package com.orosirian.trade.user.db.dao.impl;

import com.orosirian.trade.user.db.dao.UserDao;
import com.orosirian.trade.user.db.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserMapper userMapper;

    public boolean insertUser(String userName, String loginPassword, String tags) {
        int result = userMapper.insertUser(userName, loginPassword, tags);
        return result > 0;
    }

    public String selectLoginPassword(String userName) {
        return userMapper.selectLoginPassword(userName);
    }

    public boolean isUserNameExist(String userName) {
        return userMapper.isUserNameExist(userName);
    }

}
