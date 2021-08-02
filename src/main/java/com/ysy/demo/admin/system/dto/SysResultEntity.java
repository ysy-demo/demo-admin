package com.ysy.demo.admin.system.dto;

import com.ysy.demo.admin.core.model.ResultEntity;

public class SysResultEntity {

    public static final ResultEntity LOGIN_USER_NOT_EXIST = ResultEntity.invalid("Username or password is wrong!");
    public static final ResultEntity LOGIN_PASSWORD_ERROR = ResultEntity.invalid("Username or password is wrong!");
    public static final ResultEntity LOGIN_USER_LOCKED = ResultEntity.invalid("The user has been locked, please contact the administrator!");
    public static final ResultEntity ROLE_PERMISSION_DENIED = ResultEntity.invalid("Role permission denied!");
    public static final ResultEntity LOCKED = ResultEntity.invalid("Processing");
    public static final ResultEntity DEPT_NAME_ALREADY_EXISTS = ResultEntity.invalid("Dept name already exists");
    public static final ResultEntity DEPT_HAS_BEEN_USED = ResultEntity.invalid("Dept has been used, please check");
    public static final ResultEntity DICT_CODE_ALREADY_EXISTS = ResultEntity.invalid("Dict code already exists");
    public static final ResultEntity MENU_NAME_ALREADY_EXISTS = ResultEntity.invalid("Menu name already exists");
    public static final ResultEntity ROLE_NAME_ALREADY_EXISTS = ResultEntity.invalid("Role name already exists");
    public static final ResultEntity ROLE_HAS_BEEN_USED = ResultEntity.invalid("Role has been used, please check");
    public static final ResultEntity USERNAME_ALREADY_EXISTS = ResultEntity.invalid("Username already exists");
    public static final ResultEntity USER_ADD_SEND_MAIL_FAILED = ResultEntity.invalid("The user is added successfully, but the email password failed to be sent, please check the email or reset the password!");

}
