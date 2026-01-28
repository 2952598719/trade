package com.orosirian.trade.coupon.mq;

import com.alibaba.fastjson.JSONObject;
import com.orosirian.trade.coupon.db.mappers.IdempotentMapper;
import com.orosirian.trade.coupon.db.mappers.TaskMapper;
import com.orosirian.trade.coupon.db.model.Task;
import com.orosirian.trade.coupon.db.model.TaskIdempotent;
import com.orosirian.trade.coupon.db.model.TaskSend;
import com.orosirian.trade.coupon.service.CouponSendService;
import com.orosirian.trade.coupon.utils.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MessageReceiver {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private IdempotentMapper idempotentMapper;

    @Autowired
    private CouponSendService couponSendService;

    @KafkaListener(topics = {"test-topic"})
    public void consumeMessage(String message) {
        log.info("received message:{}", message);
        System.out.println("接收到消息 message:" + message);
    }

    @KafkaListener(topics = {"send-batch-coupon"})
    public void consumeBatchCouponMessage(String message) {
        log.info("received send-batch-coupon message:{}", message);
        // 1 解析消息内容
        Task task = JSONObject.parseObject(message, Task.class);
        TaskSend sendCouponTaskModel = JSONObject.parseObject(task.getBizParam(), TaskSend.class);
        try {
            // 2 查询幂等表，校验当前的这个业务是否执行执行被执行过了
            TaskIdempotent idempotent = idempotentMapper.queryIdempotentByBiz(task.getBizType(), task.getBizId());
            if (idempotent != null) {   // 不为空，说明已经执行过了 （消息之前已经被消费过了），不往下继续执行
                log.error("message is consumed:{}", message);
                return;
            }
            // 3 给用户发券
            boolean result = couponSendService.sendUserCouponSynWithLock(sendCouponTaskModel.getBatchId(), sendCouponTaskModel.getUserId());
            // 4 更新任务的状态: 1.失败了 重试次数+1 2.成功了 把状态改为已完成
            if (result) {
                task.setStatus(1);  // 成功了 把状态改为已完成
                log.info("consumeBatchCouponMessage success:{}", task.getBizParam());
                TaskIdempotent curIdempotent = new TaskIdempotent();
                curIdempotent.setBizType(task.getBizType());
                curIdempotent.setBizId(task.getBizId());
                curIdempotent.setCreateTime(LocalDateTime.now());
                idempotentMapper.insertIdempotent(curIdempotent);
            } else {
                throw new BizException("sendUserCouponSynWithLock error");
            }


        } catch (Exception ex) {
            task.setStatus(2);
            task.setRetryCount(task.getRetryCount() + 1);   // 失败了，重试次数+1
            log.info("consumeBatchCouponMessage failed sendCouponTaskModel:{} ", task.getBizParam(), ex);
        }
        task.setModifiedTime(LocalDateTime.now());
        taskMapper.updateTask(task);
    }

}
