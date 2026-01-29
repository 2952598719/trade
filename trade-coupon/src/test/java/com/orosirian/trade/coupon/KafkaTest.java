package com.orosirian.trade.coupon;

import com.orosirian.trade.coupon.mq.MessageSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KafkaTest {

    @Autowired
    private MessageSender messageSender;

    @Test
    public void sendMessage() {
        String message = "测试";
        messageSender.send("test-topic", message);
    }

}
