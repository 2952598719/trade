package com.orosirian.trade.coupon;

import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.controller.CouponBatchController;
import com.orosirian.trade.coupon.db.dao.CouponBatchDao;
import com.orosirian.trade.coupon.db.model.CouponBatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

@SpringBootTest
public class CouponBatchTest {

    @Autowired
    private CouponBatchController couponBatchController;

    @Autowired
    private CouponBatchDao couponBatchDao;

    @Test
    public void addCouponBatchActionTest() {
        assertThat(couponBatchController.addCouponBatchAction(
                "优惠券批次测试",
                "满100减20优惠券",
                1,
                1,
                100L,
                LocalDateTime.now().toString(),
                LocalDateTime.of(2026, 12, 31, 23, 59, 59).toString(),
                100,
                20,
                null
        )).isEqualTo("coupon_batch_list");
    }

    @Test
    public void deleteGoodsTest() {
        boolean deleteResult = couponBatchDao.deleteCouponBatchById(11);     // 去看mysql里有没有对应数据然后再运行
        assertThat(deleteResult).isTrue();
    }

    @Test
    public void queryGoodsTest() {
        CouponBatch goods = couponBatchDao.queryCouponBatchById(12);
        System.out.println(JSON.toJSONString(goods));
    }

    @Test
    public void updateGoods() {
        CouponBatch couponBatch = couponBatchDao.queryCouponBatchById(12);
        couponBatch.setBatchName(couponBatch.getBatchName() + " update");
        boolean updateResult = couponBatchDao.updateCouponBatch(couponBatch);
        assertThat(updateResult).isTrue();
    }
}
