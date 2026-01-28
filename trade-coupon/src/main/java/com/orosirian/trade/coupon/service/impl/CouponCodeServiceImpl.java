package com.orosirian.trade.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.db.mappers.CouponBatchMapper;
import com.orosirian.trade.coupon.db.mappers.CouponCodeMapper;
import com.orosirian.trade.coupon.db.model.CouponCode;
import com.orosirian.trade.coupon.service.CouponCodeService;
import com.orosirian.trade.coupon.utils.Constants;
import com.orosirian.trade.coupon.utils.code.ActivationCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class CouponCodeServiceImpl implements CouponCodeService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CouponCodeMapper couponCodeMapper;

    @Autowired
    private CouponBatchMapper couponBatchMapper;

    @Override
    public boolean createCouponCodeList(long batchId, int codeNum) {
        // 1 判断batchId存在性
        if (!couponBatchMapper.isBatchExist(batchId)) {
            log.error("batch {} 不存在", batchId);
            return false;
        }
        // 2 获取groupId
        int groupId = 1;
        String groupIdStr = stringRedisTemplate.opsForValue().get(Constants.COUPON_CODE_GROUP_KEY);
        if (groupIdStr != null) {
            groupId = Integer.parseInt(groupIdStr);
            stringRedisTemplate.opsForValue().increment(Constants.COUPON_CODE_GROUP_KEY);
        }
        // 3 生成券码
        List<String> codes = ActivationCodeUtil.createCodeList(groupId, codeNum, 12, Constants.COUPON_CODE_PASSWORD);
        log.info("create codes;{}", JSON.toJSONString(codes));
        // 4 保存到数据库
        for (String code : codes) {
            CouponCode couponCode = new CouponCode();
            couponCode.setCode(code);
            couponCode.setBatchId(batchId);
            couponCode.setStatus(0);
            couponCode.setCreateTime(Instant.now());
            couponCode.setModifyTime(Instant.now());
            couponCodeMapper.insertCouponCode(couponCode);
        }
        return true;
    }

    @Override
    public List<CouponCode> queryCouponCodeByBatch(long batchId) {
        return couponCodeMapper.queryCouponCodeByBatch(batchId);
    }

    @Override
    public CouponCode getCouponCode(String couponCode) {
        // 1 格式是否正确
        boolean res = ActivationCodeUtil.VerifyCode(couponCode);
        if (!res) {
            log.warn("无效兑换码: {}", couponCode);
            return null;
        }
        // 2 是否已经使用
        CouponCode couponCodeInfo = couponCodeMapper.queryCouponCodeByCode(couponCode);
        if (couponCodeInfo == null) {
            log.warn("无效兑换码: {}", couponCode);
            return null;
        }
        if (couponCodeInfo.getStatus() != 0) {
            log.warn("兑换码已使用: {}", couponCode);
            return null;
        }
        return couponCodeInfo;
    }

    @Override
    public boolean updateCouponCode(CouponCode couponCode) {
        return couponCodeMapper.updateCouponCode(couponCode) > 0;
    }

}
