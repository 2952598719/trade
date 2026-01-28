package com.orosirian.trade.coupon;

import com.orosirian.trade.coupon.db.model.CouponCode;
import com.orosirian.trade.coupon.service.CouponCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CouponCodeTest {

    @Autowired
    private CouponCodeService couponCodeService;

    @Test
    public void createCodeTest() {
        couponCodeService.createCouponCodeList(1, 10);
        for(CouponCode code: couponCodeService.queryCouponCodeByBatch(1)){
            System.out.println(code.getCode());
        }
    }

}
