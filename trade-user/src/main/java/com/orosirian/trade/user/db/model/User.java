package com.orosirian.trade.user.db.model;

import lombok.Data;

@Data
public class User {

    private Long id;

    private String userName;

    private String loginPassword;

    private String tags;

}
