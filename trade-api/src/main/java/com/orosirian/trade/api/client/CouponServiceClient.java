package com.orosirian.trade.api.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CouponServiceClient {

    private final static String couponServiceUrl = "http://localhost:8083";

    private final RestTemplate restTemplate = new RestTemplate();

    public String addCouponBatchAction(String batchName,
                                       String couponName,
                                       int couponType,
                                       int grantType,
                                       long totalCount,
                                       String startTime,
                                       String endTime,
                                       int thresholdAmount,
                                       int discountAmount
    ) {
        String params = String.format("?batchName=%s&couponName=%s&couponType=%s&grantType=%s&totalCount=%s&startTime=%s&endTime=%s&thresholdAmount=%s&discountAmount=%s",
                batchName, couponName, couponType, grantType, totalCount, startTime, endTime, thresholdAmount, discountAmount);
        return restTemplate.postForObject(
            couponServiceUrl + "/batch/addCouponBatch" + params,
            null,
            String.class
        );
    }

    public String sendCouponSynWithLock(long batchId, long userId) {
        String params = String.format("?batchId=%d&userId=%d", batchId, userId);
        return restTemplate.postForObject(
            couponServiceUrl + "/send/sendSynWithLock" + params,
            null,
            String.class
        );
    }

    public String queryUserCouponList(long userId) {
        String params = String.format("?userId=%d", userId);
        return restTemplate.getForObject(
            couponServiceUrl + "/query/user" + params,
            String.class
        );
    }

    public String queryUserCouponListWithoutCache(long userId) {
        String params = String.format("?userId=%d", userId);
        return restTemplate.getForObject(
                couponServiceUrl + "/query/user/withoutcache" + params,
                String.class
        );
    }

    public String sendUserCouponBatch(long batchId, String userIds) {
        String params = String.format("?batchId=%d&userIds=%s", batchId, userIds);
        return restTemplate.postForObject(
                couponServiceUrl + "/send/sendBatch" + params,
                null,
                String.class
        );
    }

}
