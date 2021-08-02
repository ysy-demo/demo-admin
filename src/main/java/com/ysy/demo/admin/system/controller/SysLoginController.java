package com.ysy.demo.admin.system.controller;

import com.ysy.demo.admin.core.SubjectContext;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.system.biz.SysLoginBiz;
import com.ysy.demo.admin.system.dto.LoginReq;
import com.ysy.demo.admin.system.dto.LoginRes;
import com.ysy.demo.admin.core.Subject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "登录接口")
@RestController
public class SysLoginController {

    @Autowired
    private SysLoginBiz sysLoginBiz;

    @ApiOperation("登录")
    @PostMapping("/login")
    public ResultEntity<LoginRes> login(@Valid @RequestBody LoginReq req) {
        Subject subject = SubjectContext.get();
        return sysLoginBiz.login(req, subject);
    }
}
