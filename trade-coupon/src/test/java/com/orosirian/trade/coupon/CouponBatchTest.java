package com.orosirian.trade.coupon;

import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.controller.CouponBatchController;
import com.orosirian.trade.coupon.db.mappers.CouponBatchMapper;
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
    private CouponBatchMapper couponBatchMapper;

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
        boolean deleteResult = couponBatchMapper.deleteCouponBatchById(13L) > 0;     // 去看mysql里有没有对应数据然后再运行
        assertThat(deleteResult).isTrue();
    }

    @Test
    public void queryGoodsTest() {
        CouponBatch goods = couponBatchMapper.queryCouponBatchById(12L);
        System.out.println(JSON.toJSONString(goods));
    }

    @Test
    public void updateGoods() {
        CouponBatch couponBatch = couponBatchMapper.queryCouponBatchById(12L);
        couponBatch.setBatchName(couponBatch.getBatchName() + " update");
        boolean updateResult = couponBatchMapper.updateCouponBatchSelective(couponBatch) > 0;
        assertThat(updateResult).isTrue();
    }
}
