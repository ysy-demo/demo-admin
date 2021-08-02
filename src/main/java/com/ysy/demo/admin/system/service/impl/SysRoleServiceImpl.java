package com.ysy.demo.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ysy.demo.admin.enums.YesNoEnum;
import com.ysy.demo.admin.system.entity.SysRole;
import com.ysy.demo.admin.system.entity.SysUserRole;
import com.ysy.demo.admin.system.mapper.SysRoleMapper;
import com.ysy.demo.admin.system.service.SysRoleService;
import com.ysy.demo.admin.system.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public List<SysRole> getByUserId(Long userId) {
        List<Long> roleIds = sysUserRoleService.lambdaQuery().eq(SysUserRole::getUserId, userId)
                .eq(SysUserRole::getDeleteFlag, YesNoEnum.NO.getValue()).list()
                .stream().map(e -> e.getRoleId()).collect(Collectors.toList());
        return lambdaQuery().eq(SysRole::getDeleteFlag, YesNoEnum.NO.getValue()).in(SysRole::getId, roleIds).list();
    }

}
