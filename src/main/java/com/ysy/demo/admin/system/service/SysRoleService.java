package com.ysy.demo.admin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ysy.demo.admin.system.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    List<SysRole> getByUserId(Long userId);

}
