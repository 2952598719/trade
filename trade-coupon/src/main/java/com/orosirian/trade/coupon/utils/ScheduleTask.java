package com.orosirian.trade.coupon.utils;


import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.db.mappers.CouponMapper;
import com.orosirian.trade.coupon.db.mappers.TaskMapper;
import com.orosirian.trade.coupon.db.model.Task;
import com.orosirian.trade.coupon.db.model.TaskSend;
import com.orosirian.trade.coupon.service.CouponRemindService;
import com.orosirian.trade.coupon.service.CouponSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class ScheduleTask {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponSendService couponSendService;

    @Autowired
    private CouponRemindService couponRemindService;

    @Scheduled(cron = "0/30 * * * * ?")
    private void couponFailedTaskRetry() {
        System.out.println("执行发券定时任务时间: " + Instant.now().toString());
        List<Task> tasks = taskMapper.queryFailedTaskList();
        for (Task task : tasks) {
            log.info("processing failed task : {}", JSON.toJSONString(task));
            TaskSend taskSend = JSON.parseObject(task.getBizParam(), TaskSend.class);
            boolean result = couponSendService.sendUserCouponSynWithLock(taskSend.getBatchId(), taskSend.getUserId());
            if (result) {
                task.setStatus(1);  // 已完成状态
                log.info("task retry succeed: {}", task.getBizParam());
            } else {
                task.setStatus(2);
                task.setRetryCount(task.getRetryCount() + 1);
                log.info("task retry failed: {} ", task.getBizParam());
            }
            task.setModifiedTime(Instant.now());
            taskMapper.updateTask(task);
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    private void couponRemindTask() {
        log.info("执行提醒使用券任务: {}", Instant.now().toString());
        couponRemindService.runCouponRemindTask();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void updateExpiredCoupon() {
        log.info("所有已过期的券失效");
        couponMapper.updateExpiredCoupon();
    }

}
