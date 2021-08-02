package com.ysy.demo.admin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ysy.demo.admin.system.entity.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> getByRoleIds(List<Long> roleIds);

    boolean hasByRoleIds(List<Long> roleIds);
}
