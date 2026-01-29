package com.orosirian.trade.coupon.service.impl;


import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.db.mappers.CouponBatchMapper;
import com.orosirian.trade.coupon.db.mappers.CouponMapper;
import com.orosirian.trade.coupon.db.mappers.TaskMapper;
import com.orosirian.trade.coupon.db.model.*;
import com.orosirian.trade.coupon.mq.MessageSender;
import com.orosirian.trade.coupon.service.CouponRemindService;
import com.orosirian.trade.coupon.service.CouponSendService;
import com.orosirian.trade.coupon.utils.BizException;
import com.orosirian.trade.coupon.utils.Constants;
import com.orosirian.trade.coupon.utils.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class CouponSendServiceImpl implements CouponSendService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DistributedLock distributedLock;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private CouponBatchMapper couponBatchMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private CouponRemindService couponRemindService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendUserCouponSynWithLock(long batchId, long userId) {

        String requestId = UUID.randomUUID().toString();
        try {
            if (distributedLock.tryGetLock(Constants.LOCK_KEY, requestId, Duration.ofSeconds(Constants.EXPIRE_TIME_SECOND))) {
                return sendUserCouponSyn(batchId, userId);
            }
        } catch (Exception e) {
            log.error("sendUserCouponSynWithLock error batchId={} userId={}", batchId, userId, e);
            throw new BizException(e.getMessage());
        } finally {
            boolean _ = distributedLock.releaseLock(Constants.LOCK_KEY, requestId);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendUserCouponSyn(long batchId, long userId) {   // 同步发券给单个用户
        // 执行之前或许可以通过OpenFeign向User服务查询userId是否存在
        // 1. 查询batch信息
        CouponBatch couponBatch = couponBatchMapper.queryCouponBatchById(batchId);
        if (couponBatch == null) {
            log.error("coupon batch is not exist: batchId={}, userId={}", batchId, userId);
            throw new BizException("券批次信息不存在");
        }
        // 2. 判断该batch中券余量
        if (couponBatch.getAvailableCount() <= 0) {
            log.error("couponBatch availableCount is not enough: batchId={}, userId={}", batchId, userId);
            throw new BizException("券余量不足");
        }
        // 3. 券批次表更新：券余量、已发送数量
        boolean updateSendCouponBatchRes = couponBatchMapper.updateSendCouponBatchCount(couponBatch.getId()) > 0;
        if (!updateSendCouponBatchRes) {
            log.error("update couponBatch remains failed: batchId={}, userId={}", batchId, userId);
            throw new BizException("更新券数量失败");
        }
        // 4. 券表新增：该用户获取优惠券记录
        // 由于insertCoupon的xml标签具有useGeneratedKeys="true" keyProperty="id"，因此插入结束后coupon自动附加上了id
        Coupon coupon = createCoupon(couponBatch, userId);
        boolean insertCouponRes = couponMapper.insertCoupon(coupon) > 0;
        if (!insertCouponRes) {
            log.error("insert coupon failed: batchId={}, userId={}", batchId, userId);
            throw new BizException("新增该用户券记录失败");
        }
        // 5. 插入券过期提醒任务
        boolean _ = couponRemindService.insertRemindTask(userId, coupon.getId(), JSON.parseObject(couponBatch.getRule(), CouponRule.class));
        // 6. 现在的主流做法就是先更新数据库，再删除缓存
        stringRedisTemplate.delete(Constants.LIST_KEY_PREFIX + userId);

        log.info("sendUserCouponSyn success: coupon info: {}", JSON.toJSONString(coupon));
        return true;
    }

    @Override
    public boolean sendUserCouponBatch(long batchId, Set<Long> userIdSet) {
        boolean allSuccess = true;
        for (long userId : userIdSet) {
            TaskSend taskSend = new TaskSend(batchId, userId);

            Task task = new Task();
            task.setStatus(0);
            task.setRetryCount(0);
            task.setBizType("send_coupon");
            task.setBizId(UUID.randomUUID().toString());
            task.setBizParam(JSON.toJSONString(taskSend));
            task.setModifiedTime(Instant.now());
            task.setCreateTime(Instant.now());

            // 由于insertTask的xml标签具有useGeneratedKeys="true" keyProperty="id"，因此插入结束后task自动附加上了id
            boolean res = taskMapper.insertTask(task) > 0;
            if (res) {  // 插入任务记录，成功再发送消息
                // 进kafka容器的/opt/kafka，bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic send-batch-coupon
                messageSender.send("send-batch-coupon", JSON.toJSONString(task));
            } else {
                allSuccess = false;
                log.error("insert task table error userId:{} batchId:{}", userId, batchId);
            }
        }
        return allSuccess;
    }

    public Coupon createCoupon(CouponBatch couponBatch, long userId) {
        String rule = couponBatch.getRule();
        if (rule == null || rule.isEmpty()) {
            log.error("couponBatch rule is empty: batchId={}, userId={}", couponBatch.getId(), userId);
            throw new BizException("券批次规则为空");
        }
        CouponRule couponRule = JSON.parseObject(rule, CouponRule.class);

        Coupon coupon = new Coupon();
        coupon.setUserId(userId);
        coupon.setBatchId(couponBatch.getId());
        coupon.setReceivedTime(Instant.now());     // 当前系统日期
        coupon.setValidateTime(couponRule.getEndTime());
        coupon.setCouponName(couponBatch.getCouponName());
        coupon.setStatus(0);    // 默认有效
        coupon.setCreateTime(Instant.now());
        return coupon;
    }

}
