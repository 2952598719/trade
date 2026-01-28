package com.orosirian.trade.coupon.utils;

public class Constants {

    public static int MAX_RETRY_COUNT = 5;

    public static long INIT_WAIT_TIME_MS = 100;

    public static long MAX_WAIT_TIME_MS = 1000;

    public static int WAIT_TIME_DISTURB = 50;

    public static int EXPIRE_TIME_SECOND = 30;

    public static String LOCK_KEY = "sendUserCouponLock";

    public static String LIST_KEY_PREFIX = "coupon:list:";

    public static String RULE_KEY_PREFIX = "coupon:rule:";

    public static int REMIND_BEFORE_DAYS = 3;   // 提前三天提醒

}
