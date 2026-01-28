CREATE DATABASE trade;

USE trade;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
    `login_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
    `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户的标签',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS `coupon_batch`;
CREATE TABLE `coupon_batch` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '批次id',
    `batch_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批次名称',
    `coupon_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券名称',
    `coupon_type` int NOT NULL COMMENT '优惠券类型：1-满减，2-满折，3-直减',
    `grant_type` int NOT NULL COMMENT '发放类型：1-用户领取，2-系统自动发放，3-兑换码兑换',
    `total_count` bigint NOT NULL COMMENT '总的数量',
    `available_count` bigint NOT NULL COMMENT '剩余可发放数量',
    `assign_count` bigint NOT NULL COMMENT '已发送的数量',
    `used_count` bigint NOT NULL COMMENT '用户已使用的数量',
    `rule` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券规则：规则对象对应JSON',
    `status` int NOT NULL COMMENT '活动的状态1有效，-1无效',
    `create_time` TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS `coupon_user`;
CREATE TABLE `coupon_user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '优惠券的id',
    `user_id` bigint NOT NULL COMMENT '用户id',
    `batch_id` bigint NOT NULL COMMENT '批次id',
    `coupon_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '优惠券名称',
    `status` int DEFAULT NULL COMMENT '0-未使用、1-已使用、2-已过期、3-冻结',
    `order_id` bigint DEFAULT NULL COMMENT '用在哪个订单',
    `received_time` TIMESTAMP DEFAULT NULL COMMENT '领取时间',
    `validate_time` TIMESTAMP DEFAULT NULL COMMENT '有效日期',
    `used_time` TIMESTAMP DEFAULT NULL COMMENT '使用时间',
    `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务id',
    `status` INT NOT NULL COMMENT '任务状态，0-未完成，1-已完成，2-已失败',
    `retry_count` INT NOT NULL COMMENT '重试次数',
    `biz_type` VARCHAR(64) NOT NULL COMMENT '任务类型描述',
    `biz_id` VARCHAR(128) NOT NULL COMMENT '任务id',
    `biz_param` VARCHAR(255) NOT NULL COMMENT '任务信息',
    `schedule_time` TIMESTAMP DEFAULT NULL COMMENT '计划完成时间，用于任务类型：过期提醒',
    `modified_time` TIMESTAMP DEFAULT NULL COMMENT '状态更新时间',
    `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS `idempotent`;
CREATE TABLE `idempotent` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `biz_type` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务类型',
    `biz_id` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务ID',
    `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique` (`biz_type`,`biz_id`) USING BTREE COMMENT '唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;