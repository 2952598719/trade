package com.orosirian.trade.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.db.model.CouponBatch;
import com.orosirian.trade.coupon.db.model.CouponRule;
import com.orosirian.trade.coupon.mq.MessageSender;
import com.orosirian.trade.coupon.service.CouponBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CouponBatchController {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private CouponBatchService couponBatchService;

    @PostMapping("/batch/addCouponBatch")
    public String addCouponBatch(
            @RequestParam("batchName") String batchName,
            @RequestParam("couponName") String couponName,
            @RequestParam("couponType") int couponType,
            @RequestParam("grantType") int grantType,
            @RequestParam("totalCount") long totalCount,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("thresholdAmount") int thresholdAmount,
            @RequestParam("discountAmount") int discountAmount
    ) {
        // 构造优惠券规则
        CouponRule couponRule = new CouponRule();
        couponRule.setCouponType(couponType);
        couponRule.setGrantType(grantType);
        couponRule.setStartTime(LocalDateTime.parse(startTime));  // 原先格式类似2026-01-26T17:24:30
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
        couponBatch.setAvailableCount(totalCount);
        couponBatch.setAssignCount(0L);
        couponBatch.setUsedCount(0L);
        couponBatch.setRule(JSON.toJSONString(couponRule));
        couponBatch.setStatus(1);   // 默认状态有效
        couponBatch.setCreateTime(LocalDateTime.now());
        boolean _ = couponBatchService.insertCouponBatch(couponBatch);
        log.info("addCouponBatchAction success couponBatch:{}", JSON.toJSONString(couponRule));
        return "coupon_batch_list";     // 跳转到券批次列表
    }

    @GetMapping("/batch/couponBatchList")
    public String couponBatchList(Map<String, Object> resultMap) {
        List<CouponBatch> couponBatchList = couponBatchService.queryCouponBatchList();
        resultMap.put("couponBatchList", couponBatchList);
        return "coupon_batch_list";
    }

    @PostMapping("/kafka")
    public void kafkaTest() {
        messageSender.send("test-topic", "习近平");
    }

}
