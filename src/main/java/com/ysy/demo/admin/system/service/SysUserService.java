package com.ysy.demo.admin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ysy.demo.admin.system.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

}
