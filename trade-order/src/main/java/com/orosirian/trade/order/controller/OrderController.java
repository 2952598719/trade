package com.orosirian.trade.order.controller;

import com.orosirian.trade.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    @ResponseBody
    public String order(long userId, long orderId, long couponId) {
        boolean tryStage = orderService.createOrder(userId, orderId, couponId);
        if (!tryStage) {
            return "订单失败";
        }
        boolean confirmStage = true;    // 假设收到了所有服务的正常响应
        if (confirmStage) {
            orderService.paySuccess(userId, orderId, couponId);
            return "订单成功";
        } else {
            orderService.closeOrder(userId, orderId, couponId);
            return "订单失败";
        }
    }

}
