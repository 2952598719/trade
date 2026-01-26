package com.orosirian.trade.coupon;


import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.db.model.CouponRule;
import com.orosirian.trade.coupon.service.CouponSendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CouponSendTest {

    @Autowired
    private CouponSendService couponSendService;

    @Test
    public void sendCouponTest() {
        assertThat(couponSendService.sendUserCouponSyn(12L, 1L)).isTrue();
    }

}
