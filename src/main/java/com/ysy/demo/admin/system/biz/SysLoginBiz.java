package com.ysy.demo.admin.system.biz;

import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.system.dto.LoginReq;
import com.ysy.demo.admin.core.Subject;
import com.ysy.demo.admin.system.dto.LoginRes;

public interface SysLoginBiz {

    ResultEntity<LoginRes> login(LoginReq req, Subject subject);
}
