package com.ysy.demo.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ysy.demo.admin.enums.YesNoEnum;
import com.ysy.demo.admin.system.entity.SysMenu;
import com.ysy.demo.admin.system.entity.SysRoleMenu;
import com.ysy.demo.admin.system.mapper.SysMenuMapper;
import com.ysy.demo.admin.system.service.SysMenuService;
import com.ysy.demo.admin.system.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenu> getByRoleIds(List<Long> roleIds) {
        List<SysRoleMenu> roleMenus = sysRoleMenuService.lambdaQuery()
                .eq(SysRoleMenu::getDeleteFlag, YesNoEnum.NO.getValue())
                .in(SysRoleMenu::getRoleId, roleIds).list();
        if (roleMenus.isEmpty()) {
            return Collections.emptyList();
        }
        return lambdaQuery().eq(SysMenu::getDeleteFlag, YesNoEnum.NO.getValue())
                .in(SysMenu::getId, roleMenus.stream().map(e -> e.getMenuId())
                        .collect(Collectors.toList())).list();
    }

    @Override
    public boolean hasByRoleIds(List<Long> roleIds) {
        List<SysRoleMenu> roleMenus = sysRoleMenuService.lambdaQuery()
                .eq(SysRoleMenu::getDeleteFlag, YesNoEnum.NO.getValue())
                .in(SysRoleMenu::getRoleId, roleIds).list();
        if (roleMenus.isEmpty()) {
            return false;
        }
        return lambdaQuery().select(SysMenu::getId).eq(SysMenu::getDeleteFlag, YesNoEnum.NO.getValue())
                .in(SysMenu::getId, roleMenus.stream().map(e -> e.getMenuId())
                        .collect(Collectors.toList())).last("LIMIT 1").one() != null;
    }
}
