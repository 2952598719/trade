package com.orosirian.trade.order.service.impl;

import com.orosirian.trade.order.service.CouponTransactionService;
import com.orosirian.trade.order.service.GoodsTransactionService;
import com.orosirian.trade.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    // Try: createOrder，锁定资源，这一步如果有不ok的就已经应该直接停掉了
    // Confirm: paySuccess
    // Cancel: closeOrder

    @Autowired
    private GoodsTransactionService goodsTransactionService;

    @Autowired
    private CouponTransactionService couponTransactionService;

    @Override
    public boolean createOrder(long userId, long couponId, long orderId) {
        // 1 尝试锁定库存与优惠券
        boolean tryGoodsStockResult = goodsTransactionService.tryGoodsStock();
        boolean tryCouponResult = couponTransactionService.tryCoupon(userId, couponId, orderId);
        // 2 如果失败就cancel
        if (!tryGoodsStockResult || !tryCouponResult) {
            log.error("rollback tryGoodsStockResult: {}, tryCouponResult: {}", tryGoodsStockResult, tryCouponResult);
            goodsTransactionService.cancelGoodsStock();
            couponTransactionService.cancelCoupon(userId, couponId, orderId);
            return false;
        }
        return true;
    }

    @Override
    public void paySuccess(long userId, long couponId, long orderId) {
        goodsTransactionService.commitGoodsStock();
        couponTransactionService.commitCoupon(userId, couponId, orderId);
    }

    @Override
    public void closeOrder(long userId, long couponId, long orderId) {
        // 关闭订单时候，要把前面所有try的资源都回滚掉
        goodsTransactionService.cancelGoodsStock();
        couponTransactionService.cancelCoupon(userId, couponId, orderId);
    }

}
