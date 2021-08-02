package com.ysy.demo.admin.system.service;

import com.ysy.demo.admin.core.model.HandelResult;

public interface MailService {

    HandelResult send(String code, String to, Object... params);

}
