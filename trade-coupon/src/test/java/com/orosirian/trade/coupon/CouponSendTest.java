package com.orosirian.trade.coupon;

import com.orosirian.trade.coupon.service.CouponSendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CouponSendTest {

    @Autowired
    private CouponSendService couponSendService;

    @Test
    public void sendCouponTest() {
        assertThat(couponSendService.sendUserCouponSyn(1L, 1L)).isTrue();
    }

    @Test
    public void batchSendCouponTest() {
        Set<Long> set = new HashSet<>();
        set.add(1L);
        set.add(2L);
        set.add(3L);
        set.add(4L);
        couponSendService.sendUserCouponBatch(1, set);
    }

}
