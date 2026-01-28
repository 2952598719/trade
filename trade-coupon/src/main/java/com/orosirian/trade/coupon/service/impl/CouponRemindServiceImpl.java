package com.orosirian.trade.coupon.service.impl;


import com.alibaba.fastjson.JSON;
import com.orosirian.trade.coupon.db.mappers.CouponMapper;
import com.orosirian.trade.coupon.db.mappers.TaskMapper;
import com.orosirian.trade.coupon.db.model.Coupon;
import com.orosirian.trade.coupon.db.model.CouponRule;
import com.orosirian.trade.coupon.db.model.Task;
import com.orosirian.trade.coupon.db.model.TaskRemind;
import com.orosirian.trade.coupon.service.CouponRemindService;
import com.orosirian.trade.coupon.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CouponRemindServiceImpl implements CouponRemindService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public boolean insertRemindTask(long userId, long couponId, CouponRule rule) {
        TaskRemind taskRemind = new TaskRemind();
        taskRemind.setUserId(userId);
        taskRemind.setCouponId(couponId);
        taskRemind.setRemindTime(rule.getEndTime().minusDays(Constants.REMIND_BEFORE_DAYS));

        Task task = new Task();
        task.setStatus(0);
        task.setRetryCount(0);
        task.setBizType("remind_coupon");
        task.setBizId(UUID.randomUUID().toString());
        task.setBizParam(JSON.toJSONString(taskRemind));
        task.setScheduleTime(rule.getEndTime().minusDays(Constants.REMIND_BEFORE_DAYS));
        task.setModifiedTime(LocalDateTime.now());
        task.setCreateTime(LocalDateTime.now());
        log.info("insertCouponRemindTask ,task:{}", JSON.toJSONString(task));
        return taskMapper.insertTask(task);
    }

    @Override
    public void runCouponRemindTask() {
        List<Task> list = taskMapper.queryRemindTaskList();
        for (Task task : list) {
            log.info("runCouponRemindTask task:{}", JSON.toJSONString(task));
            TaskRemind taskRemind = JSON.parseObject(task.getBizParam(), TaskRemind.class);

            // 查询券是否无效（已使用、已过期、冻结）
            if (couponMapper.isCouponInvalid(taskRemind.getCouponId())) {
                return;
            }

            // 给用户发提醒，用log代替
            log.info("还有3天券过期了，抓紧时间使用.......");

            // 更新这个状态
            task.setStatus(1);
            taskMapper.updateTask(task);
        }
    }

}
