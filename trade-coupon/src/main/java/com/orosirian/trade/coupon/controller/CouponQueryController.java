package com.orosirian.trade.coupon.controller;


import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.db.model.Coupon;
import com.orosirian.trade.coupon.db.model.CouponRule;
import com.orosirian.trade.coupon.db.model.CouponVO;
import com.orosirian.trade.coupon.service.CouponBatchService;
import com.orosirian.trade.coupon.service.CouponQueryService;
import com.orosirian.trade.coupon.utils.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CouponQueryController {

    @Autowired
    private CouponBatchService couponBatchService;

    @Autowired
    private CouponQueryService couponQueryService;

    @GetMapping("/query/user")
    public String queryUserCouponList(@RequestParam("userId") long userId) {
        List<Coupon> couponList = couponQueryService.queryUserCouponList(userId);
        List<CouponVO> notUsedList = new ArrayList<>();
        List<CouponVO> usedList = new ArrayList<>();
        List<CouponVO> expiredList = new ArrayList<>();
        for (Coupon coupon : couponList) {
            CouponVO couponVO = createCouponVO(coupon);
            if (coupon.getStatus() == 0) {
                notUsedList.add(couponVO);
            } else if (coupon.getStatus() == 1) {
                usedList.add(couponVO);
            } else if (coupon.getStatus() == 2) {
                expiredList.add(couponVO);
            }
        }
        Map<String, List<CouponVO>> couponMap = new HashMap<>();
        couponMap.put("notUsedList", notUsedList);
        couponMap.put("usedList", usedList);
        couponMap.put("expiredList", expiredList);
        return JSON.toJSONString(couponMap);  // 不存在记录导致返回空列表也没事
    }

    @GetMapping("/query/user/withoutcache")
    public String queryUserCouponListWithoutCache(@RequestParam("userId") long userId) {
        List<Coupon> couponList = couponQueryService.queryUserCouponListWithoutCache(userId);
        List<CouponVO> notUsedList = new ArrayList<>();
        List<CouponVO> usedList = new ArrayList<>();
        List<CouponVO> expiredList = new ArrayList<>();
        for (Coupon coupon : couponList) {
            CouponVO couponVO = createCouponVO(coupon);
            if (coupon.getStatus() == 0) {
                notUsedList.add(couponVO);
            } else if (coupon.getStatus() == 1) {
                usedList.add(couponVO);
            } else if (coupon.getStatus() == 2) {
                expiredList.add(couponVO);
            }
        }
        Map<String, List<CouponVO>> couponMap = new HashMap<>();
        couponMap.put("notUsedList", notUsedList);
        couponMap.put("usedList", usedList);
        couponMap.put("expiredList", expiredList);
        return JSON.toJSONString(couponMap);  // 不存在记录导致返回空列表也没事
    }

    public CouponVO createCouponVO(Coupon coupon) {
        String ruleStr = couponBatchService.selectCouponRule(coupon.getBatchId());
        if (ruleStr == null || ruleStr.isEmpty()) {
            log.error("coupon rule not exist: batchId {}", coupon.getBatchId());
            throw new BizException("优惠券规则不存在");
        }
        CouponRule rule = JSON.parseObject(ruleStr, CouponRule.class);
        CouponVO couponVO = new CouponVO();
        couponVO.setId(coupon.getId());
        couponVO.setUserId(coupon.getUserId());
        couponVO.setBatchId(coupon.getBatchId());
        couponVO.setCouponName(coupon.getCouponName());
        couponVO.setStatus(coupon.getStatus());
        couponVO.setCouponType(rule.getCouponType());
        couponVO.setGrantType(rule.getGrantType());
        if (rule.getCouponType() == 1) {    // 满减券
            couponVO.setThresholdAmountStr(String.format("满 %d 减 %d 元", rule.getThresholdAmount(), rule.getDiscountAmount()));
        }
        couponVO.setDiscountAmount(rule.getDiscountAmount());
        couponVO.setStartTimeToEndTime(rule.getStartTime().toString() + "~" + rule.getEndTime().toString());
//        couponVO.setStartTimeToEndTime(rule.getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE) + "~" + rule.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
        return couponVO;
    }

}
