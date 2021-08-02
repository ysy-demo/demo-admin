package com.ysy.demo.admin.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.dto.BaseObjectRes;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.system.biz.SysUserBiz;
import com.ysy.demo.admin.system.dto.UserAddReq;
import com.ysy.demo.admin.system.dto.UserDetailRes;
import com.ysy.demo.admin.system.dto.UserEditAvatarReq;
import com.ysy.demo.admin.system.dto.UserEditPasswordReq;
import com.ysy.demo.admin.system.dto.UserEditReq;
import com.ysy.demo.admin.system.dto.UserListReq;
import com.ysy.demo.admin.system.dto.UserListRes;
import com.ysy.demo.admin.system.dto.UserPasswordResetReq;
import com.ysy.demo.admin.core.annotation.Log;
import com.ysy.demo.admin.core.auth.util.AuthInfoUtils;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("sys/user")
public class SysUserController {

    @Autowired
    private SysUserBiz sysUserBiz;

    @ApiOperation("列表：权限 sys_user:list")
    @GetMapping
    @RequiresPermissions("sys_user:list")
    public ResultEntity<BasePageRes<UserListRes>> list(@Valid UserListReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        BasePageRes<UserListRes> res = sysUserBiz.pageList(req, user);
        return ResultEntity.success(res);
    }

    @ApiOperation("详情：权限 sys_user:list")
    @GetMapping("{id}")
    @RequiresPermissions("sys_user:list")
    public ResultEntity<UserDetailRes> detail(@NotBlank(message = "{required}") @PathVariable Long id) {
        AuthUser user = AuthInfoUtils.getUser();
        UserDetailRes res = sysUserBiz.detail(id, user);
        return ResultEntity.success(res);
    }

    @ApiOperation("添加：权限 sys_user:add")
    @Log("新增用户")
    @PostMapping
    @RequiresPermissions("sys_user:add")
    public ResultEntity add(@Valid @RequestBody UserAddReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysUserBiz.add(req, user);
    }

    @ApiOperation("编辑：权限 sys_user:edit")
    @Log("修改用户")
    @PutMapping
    @RequiresPermissions("sys_user:edit")
    public ResultEntity edit(@Valid @RequestBody UserEditReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysUserBiz.edit(req, user);
    }

    @ApiOperation("删除：权限 sys_user:delete")
    @Log("删除用户")
    @DeleteMapping("{ids}")
    @RequiresPermissions("sys_user:delete")
    public ResultEntity delete(@NotBlank(message = "{required}") @PathVariable String ids) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysUserBiz.delete(Stream.of(ids.split(StringPool.COMMA))
                .map(id -> Long.parseLong(id)).collect(Collectors.toList()), user);
    }

    @ApiOperation("重置密码：权限 sys_user:password-reset")
    @Log("重置密码")
    @PutMapping("password/reset")
    @RequiresPermissions("sys_user:password-reset")
    public ResultEntity resetPassword(@Valid @RequestBody UserPasswordResetReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysUserBiz.resetPassword(req, user);
    }

    @ApiOperation("校验用户名")
    @GetMapping("check")
    public ResultEntity<BaseObjectRes<Boolean>> checkUserName(@NotBlank(message = "{required}") String username) {
        return sysUserBiz.checkUsername(username);
    }

    @ApiOperation("修改头像")
    @Log("修改头像")
    @PutMapping("avatar")
    public ResultEntity updateAvatar(@Valid @RequestBody UserEditAvatarReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysUserBiz.updateAvatar(req, user);
    }

    @ApiOperation("修改密码")
    @Log("修改密码")
    @PutMapping("password")
    public ResultEntity updatePassword(@Valid @RequestBody UserEditPasswordReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysUserBiz.updatePassword(req, user);
    }

}
