package com.orosirian.trade.order.service;

public interface OrderService {

    boolean createOrder(long userId, long orderId, long couponId);     // 创建订单

    void paySuccess(long userId, long orderId, long couponId);      // 支付成功

    void closeOrder(long userId, long orderId, long couponId);      // 关闭订单

}
