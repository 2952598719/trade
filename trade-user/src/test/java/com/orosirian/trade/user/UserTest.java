package com.orosirian.trade.user;

import com.orosirian.trade.user.db.model.User;
import com.orosirian.trade.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void insertUser() {
        for (int i = 1; i <= 4; i++) {
            User user = new User();
            user.setUserName("Jack" + i);
            user.setLoginPassword("111111");
            user.setTags("Rose" + i);
            assertThat(userService.insertUser(user)).isTrue();
        }
    }

    @Test
    public void checkPassword() {
        assertThat(userService.checkPassword("Jack", "111111")).isTrue();
    }

}
