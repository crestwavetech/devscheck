package ru.cwt.devscheck.model;

import java.util.Date;

/**
 * @author e.chertikhin
 * @date 12/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class User {

    String id;
    String name;
    String login;
    String password;
    String passwordHash;
    String email;
    String phone;

    int maxLoginFail;
    int loginFail;

    Date createDate;
    Date updateDate;
    Date lastLoginDate;

}
