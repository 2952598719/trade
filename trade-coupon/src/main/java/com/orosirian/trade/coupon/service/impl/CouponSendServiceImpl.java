package com.orosirian.trade.coupon.service.impl;


import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.db.mappers.CouponBatchMapper;
import com.orosirian.trade.coupon.db.mappers.CouponMapper;
import com.orosirian.trade.coupon.db.model.Coupon;
import com.orosirian.trade.coupon.db.model.CouponBatch;
import com.orosirian.trade.coupon.db.model.CouponRule;
import com.orosirian.trade.coupon.service.CouponSendService;
import com.orosirian.trade.coupon.utils.BizException;
import com.orosirian.trade.coupon.utils.Constants;
import com.orosirian.trade.coupon.utils.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class CouponSendServiceImpl implements CouponSendService {

    @Autowired
    private DistributedLock distributedLock;

    @Autowired
    private CouponBatchMapper couponBatchMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendUserCouponSynWithLock(long batchId, long userId) {
        String lockKey = "sendUserCoupon";
        String requestId = UUID.randomUUID().toString();
        try {
            if (distributedLock.tryGetLock(lockKey, requestId, Duration.ofSeconds(Constants.EXPIRE_TIME_SECOND))) {
                return sendUserCouponSyn(batchId, userId);
            }
        } catch (Exception e) {
            log.error("sendUserCouponSynWithLock error batchId={} userId={}", batchId, userId, e);
            throw new BizException(e.getMessage());
        } finally {
            boolean _ = distributedLock.releaseLock(lockKey, requestId);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendUserCouponSyn(long batchId, long userId) {   // 同步发券给单个用户
        // 1. 查询batch信息
        // TODO 是否应该检查用户表中存在userId，但这里涉及到服务间通信，先放在这
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
        Coupon coupon = createCoupon(couponBatch, userId);
        boolean insertCouponRes = couponMapper.insertCoupon(coupon) > 0;
        if (!insertCouponRes) {
            log.error("insert coupon failed: batchId={}, userId={}", batchId, userId);
            throw new BizException("新增该用户券记录失败");
        }
        log.info("sendUserCouponSyn success: coupon info: {}", JSON.toJSONString(coupon));
        return true;
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
        coupon.setReceivedTime(LocalDateTime.now());     // 当前系统日期
        coupon.setValidateTime(couponRule.getEndTime());
        coupon.setCouponName(couponBatch.getCouponName());
        coupon.setStatus(1);    // 默认有效
        coupon.setCreateTime(LocalDateTime.now());
        return coupon;
    }

}
