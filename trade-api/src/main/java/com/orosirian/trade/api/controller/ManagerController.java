package com.orosirian.trade.api.controller;

import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.db.model.CouponRule;
import com.orosirian.trade.coupon.db.model.CouponBatch;
import com.orosirian.trade.coupon.service.CouponBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Controller
public class ManagerController {

    @Autowired
    private CouponBatchService couponBatchService;

    @RequestMapping("/addCouponBatchAction")
    public String addCouponBatchAction(
            @RequestParam("batchName") String batchName,
            @RequestParam("couponName") String couponName,
            @RequestParam("couponType") int couponType,
            @RequestParam("grantType") int grantType,
            @RequestParam("totalCount") long totalCount,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("thresholdAmount") int thresholdAmount,
            @RequestParam("discountAmount") int discountAmount,
            Map<String, Object> resultMap
    ) {
        // 构造优惠券规则
        CouponRule couponRule = new CouponRule();
        couponRule.setCouponType(couponType);
        couponRule.setGrantType(grantType);
        couponRule.setStartTime(LocalDateTime.parse(startTime));  // 原先格式类似2026-01-26T17:24
        couponRule.setEndTime(LocalDateTime.parse(endTime));
        couponRule.setThresholdAmount(thresholdAmount);
        couponRule.setDiscountAmount(discountAmount);

        // 构造优惠券
        CouponBatch couponBatch = new CouponBatch();
        couponBatch.setBatchName(batchName);
        couponBatch.setCouponName(couponName);
        couponBatch.setCouponType(couponType);
        couponBatch.setGrantType(grantType);
        couponBatch.setTotalCount(totalCount);
        couponBatch.setStatus(1);   // 默认状态有效
        couponBatch.setUsedCount(0L);
        couponBatch.setAssignCount(0L);
        couponBatch.setCreateTime(LocalDateTime.now());
        couponBatch.setRule(JSON.toJSONString(couponRule));
        couponBatchService.insertCouponBatch(couponBatch);
        log.info("addCouponBatchAction success couponBatch:{}", JSON.toJSONString(couponRule));
        return "coupon_batch_list";     // 跳转到券批次列表
    }


}
