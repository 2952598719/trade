package com.orosirian.trade.user.db.mappers;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    int insertUser(String userName, String loginPassword, String tags);

    String selectLoginPassword(String userName);

    boolean isUserNameExist(String userName);

}