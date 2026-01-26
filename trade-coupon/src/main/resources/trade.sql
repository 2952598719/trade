CREATE DATABASE trade;

USE trade;

CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
    `login_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
    `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户的标签',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;


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
    `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

CREATE TABLE `coupon_user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '优惠券的id',
    `user_id` bigint NOT NULL COMMENT '用户id',
    `batch_id` bigint NOT NULL COMMENT '批次id',
    `coupon_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '优惠券名称',
    `status` int DEFAULT NULL COMMENT '0-未使用、1-已使用、2-已过期、3-冻结',
    `order_id` bigint DEFAULT NULL COMMENT '用在哪个订单',
    `received_time` datetime DEFAULT NULL COMMENT '领取时间',
    `validate_time` datetime DEFAULT NULL COMMENT '有效日期',
    `used_time` datetime DEFAULT NULL COMMENT '使用时间',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;



