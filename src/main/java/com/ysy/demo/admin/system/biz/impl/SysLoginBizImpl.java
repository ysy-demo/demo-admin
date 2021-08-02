package com.ysy.demo.admin.system.biz.impl;

import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.system.biz.SysLoginBiz;
import com.ysy.demo.admin.system.dto.LoginReq;
import com.ysy.demo.admin.system.dto.SysResultEntity;
import com.ysy.demo.admin.system.dto.UserRes;
import com.google.common.collect.Sets;
import com.ysy.demo.admin.core.Subject;
import com.ysy.demo.admin.core.auth.entity.AuthInfo;
import com.ysy.demo.admin.core.auth.AuthService;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.system.dto.LoginRes;
import com.ysy.demo.admin.system.entity.SysDept;
import com.ysy.demo.admin.system.entity.SysLoginLog;
import com.ysy.demo.admin.system.entity.SysMenu;
import com.ysy.demo.admin.system.entity.SysRole;
import com.ysy.demo.admin.system.entity.SysUser;
import com.ysy.demo.admin.system.enums.SysUserStatus;
import com.ysy.demo.admin.system.service.SysDeptService;
import com.ysy.demo.admin.system.service.SysLoginLogService;
import com.ysy.demo.admin.system.service.SysMenuService;
import com.ysy.demo.admin.system.service.SysRoleService;
import com.ysy.demo.admin.system.service.SysUserService;
import com.ysy.demo.admin.util.UserPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysLoginBizImpl implements SysLoginBiz {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysLoginLogService sysLoginLogService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private UserPasswordUtils userPasswordUtils;

    @Autowired
    private AuthService authService;

    @Override
    public ResultEntity<LoginRes> login(LoginReq req, Subject subject) {
        SysUser sysUser = sysUserService.getByUsername(req.getUsername());
        if (sysUser == null) {
            return SysResultEntity.LOGIN_USER_NOT_EXIST;
        }
        String password = userPasswordUtils.encrypt(req.getUsername(), req.getPassword());
        if (!sysUser.getPassword().equals(password)) {
            return SysResultEntity.LOGIN_PASSWORD_ERROR;
        }
        if (SysUserStatus.LOCKED.getValue().equals(sysUser.getStatus())) {
            return SysResultEntity.LOGIN_USER_LOCKED;
        }

        sysUserService.lambdaUpdate().set(SysUser::getLastLoginTime, new Date()).eq(SysUser::getId, sysUser.getId()).update();
        sysLoginLogService.save(SysLoginLog.builder().userId(sysUser.getId()).ip(subject.getIp()).build());
        LoginRes res = buildLoginRes(sysUser);
        return ResultEntity.success(res);
    }

    private LoginRes buildLoginRes(SysUser sysUser) {
        SysDept dept = sysDeptService.getById(sysUser.getDeptId());
        String deptName = dept == null ? "" : dept.getDeptName();
        LoginRes res = new LoginRes();
        res.setUser(UserRes.builder().id(sysUser.getId()).username(sysUser.getUsername()).deptId(sysUser.getDeptId())
                .deptName(deptName).email(sysUser.getEmail()).description(sysUser.getDescription())
                .avatar(sysUser.getAvatar()).lastLoginTime(sysUser.getLastLoginTime().getTime())
                .createTime(sysUser.getCreateTime().getTime()).build());

        AuthInfo authInfo = buildAuthInfo(sysUser);
        res.setRoles(authInfo.getRoles());
        res.setPermissions(authInfo.getPermissions());
        String token = authService.saveAuthInfo(authInfo);
        res.setToken(token);
        return res;
    }

    private AuthInfo buildAuthInfo(SysUser sysUser) {
        AuthInfo authInfo = AuthInfo.builder().user(AuthUser.builder().id(sysUser.getId()).username(sysUser.getUsername())
                .password(sysUser.getPassword()).roleLevel(sysUser.getRoleLevel()).build()).build();
        List<SysRole> roles = sysRoleService.getByUserId(sysUser.getId());
        if (!roles.isEmpty()) {
            List<Long> roleIds = new ArrayList<>(roles.size());
            Set<String> roleCodes = Sets.newHashSetWithExpectedSize(roles.size());
            for (SysRole r : roles) {
                roleIds.add(r.getId());
                roleCodes.add(r.getRoleCode());
            }
            List<SysMenu> permissions = sysMenuService.getByRoleIds(roleIds);
            authInfo.setRoles(roleCodes);
            authInfo.setPermissions(permissions.stream().map(SysMenu::getPerms).collect(Collectors.toSet()));
        }
        return authInfo;
    }

}
