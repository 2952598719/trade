package com.orosirian.trade.user.service.impl;

import com.orosirian.trade.user.db.dao.UserDao;
import com.orosirian.trade.user.db.model.User;
import com.orosirian.trade.user.service.UserService;
import com.orosirian.trade.user.utils.BizException;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public boolean insertUser(User user) {
        if (userDao.isUserNameExist(user.getUserName())) {
            log.error("username already existed: {}", user.getUserName());
            throw new BizException("用户名已存在");
        }
        String hashedPassword = BCrypt.hashpw(user.getLoginPassword(), BCrypt.gensalt());
        return userDao.insertUser(user.getUserName(), hashedPassword, user.getTags());
    }

    public boolean checkPassword(String userName, String loginPassword) {
        String hashedPassword = userDao.selectLoginPassword(userName);
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            log.error("user {} not exist or has a empty password", userName);
            throw new BizException("用户不存在或密码为空");
        }
        return BCrypt.checkpw(loginPassword, hashedPassword);
    }

}
