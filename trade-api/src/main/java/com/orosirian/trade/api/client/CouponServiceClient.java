package com.orosirian.trade.api.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CouponServiceClient {

    private final static String couponServiceUrl = "http://localhost:8085";

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

    public String sendUserCouponBatch(long batchId, String userIds) {
        String params = String.format("?batchId=%d&userIds=%s", batchId, userIds);
        return restTemplate.postForObject(
                couponServiceUrl + "/send/sendBatch" + params,
                null,
                String.class
        );
    }

    public String createCouponCodeList(long batchId, int codeNum) {
        String params = String.format("?batchId=%d&codeNum=%d", batchId, codeNum);
        return restTemplate.postForObject(
                couponServiceUrl + "/code/createCouponCodeList" + params,
                null,
                String.class
        );
    }

    public String exchangeCouponWithCode(long userId, String couponCode) {
        String params = String.format("?couponCode=%s", couponCode);
        return restTemplate.postForObject(
                couponServiceUrl + "/send/exchangeCouponWithCode/" + userId + params,
                null,
                String.class
        );
    }

}
