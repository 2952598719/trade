package com.orosirian.trade.user.service;

import com.orosirian.trade.user.db.model.User;

public interface UserService {

    boolean insertUser(User user);

    boolean checkPassword(String userName, String loginPassword);

}
