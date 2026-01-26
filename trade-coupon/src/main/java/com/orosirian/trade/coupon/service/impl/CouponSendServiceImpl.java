package com.orosirian.trade.coupon.service.impl;


import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.db.dao.CouponBatchDao;
import com.orosirian.trade.coupon.db.dao.CouponDao;
import com.orosirian.trade.coupon.db.model.Coupon;
import com.orosirian.trade.coupon.db.model.CouponBatch;
import com.orosirian.trade.coupon.db.model.CouponRule;
import com.orosirian.trade.coupon.service.CouponSendService;
import com.orosirian.trade.coupon.utils.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CouponSendServiceImpl implements CouponSendService {

    @Autowired
    private CouponBatchDao couponBatchDao;

    @Autowired
    private CouponDao couponDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendUserCouponSyn(long batchId, long userId) {   // 同步发券给单个用户
        // 1. 查询batch信息
        // TODO 是否应该检查用户表中存在userId，但这里涉及到服务间通信，先放在这
        CouponBatch couponBatch = couponBatchDao.queryCouponBatchById(batchId);
        if (couponBatch == null) {
            log.error("coupon batch is not exist: batchId={}, userId={}", batchId, userId);
            throw new BizException("券批次信息不存在");
        }
        // 2. 判断该batch中券余量
        if (couponBatch.getAvailableCount() <= 0) {
            log.error("couponBatch availableCount is not enough: batchId={}, userId={}", batchId, userId);
            throw new BizException("券批次中，券余量不足");
        }
        // 3. 券批次表更新：券余量、已发送数量
        boolean updateSendCouponBatchRes = couponBatchDao.updateSendCouponBatchCount(couponBatch.getId());
        if (!updateSendCouponBatchRes) {
            log.error("update couponBatch remains failed: batchId={}, userId={}", batchId, userId);
            throw new BizException("券批次中，更新券的数量失败");
        }
        // 4. 券表新增：该用户获取优惠券记录
        Coupon coupon = createCoupon(couponBatch, userId);
        boolean insertCouponRes = couponDao.insertCoupon(coupon);
        if (!insertCouponRes) {
            log.error("insert coupon failed: batchId={}, userId={}", batchId, userId);
            throw new BizException("券表中，新增该用户优惠券记录失败");
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
