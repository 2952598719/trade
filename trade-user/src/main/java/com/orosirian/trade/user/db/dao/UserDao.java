package com.orosirian.trade.user.db.dao;

public interface UserDao {

    boolean insertUser(String userName, String loginPassword, String tags);

    String selectLoginPassword(String userName);

    boolean isUserNameExist(String userName);

}
