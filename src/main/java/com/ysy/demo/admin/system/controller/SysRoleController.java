package com.ysy.demo.admin.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.system.biz.SysRoleBiz;
import com.ysy.demo.admin.system.dto.RoleAddReq;
import com.ysy.demo.admin.system.dto.RoleEditReq;
import com.ysy.demo.admin.system.dto.RoleListReq;
import com.ysy.demo.admin.system.dto.RoleListRes;
import com.ysy.demo.admin.core.annotation.Log;
import com.ysy.demo.admin.core.auth.util.AuthInfoUtils;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.system.dto.RoleDetailRes;
import com.ysy.demo.admin.system.dto.RoleEditMenuReq;
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

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleBiz sysRoleBiz;

    @ApiOperation("列表：权限 sys_role:list")
    @GetMapping
    @RequiresPermissions("sys_role:list")
    public ResultEntity<BasePageRes<RoleListRes>> list(@Valid RoleListReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        BasePageRes<RoleListRes> res = sysRoleBiz.pageList(req, user);
        return ResultEntity.success(res);
    }

    @ApiOperation("详情：权限 sys_role:list")
    @GetMapping("{id}")
    @RequiresPermissions("sys_role:list")
    public ResultEntity<RoleDetailRes> detail(@NotBlank(message = "{required}") @PathVariable Long id) {
        AuthUser user = AuthInfoUtils.getUser();
        RoleDetailRes res = sysRoleBiz.detail(id, user);
        return ResultEntity.success(res);
    }

    @ApiOperation("添加：权限 sys_role:add")
    @Log("新增角色")
    @PostMapping
    @RequiresPermissions("sys_role:add")
    public ResultEntity add(@Valid @RequestBody RoleAddReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysRoleBiz.add(req, user);
    }

    @ApiOperation("编辑：权限 sys_role:edit")
    @Log("修改角色")
    @PutMapping
    @RequiresPermissions("sys_role:edit")
    public ResultEntity edit(@Valid @RequestBody RoleEditReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysRoleBiz.edit(req, user);
    }

    @ApiOperation("编辑：权限 sys_role:edit")
    @Log("修改角色菜单")
    @PutMapping("menu")
    @RequiresPermissions("sys_role:edit")
    public ResultEntity editMenu(@Valid @RequestBody RoleEditMenuReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysRoleBiz.editMenu(req, user);
    }

    @ApiOperation("删除：权限 sys_role:delete")
    @Log("删除角色")
    @DeleteMapping("{roleIds}")
    @RequiresPermissions("sys_role:delete")
    public ResultEntity delete(@NotBlank(message = "{required}") @PathVariable String roleIds) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysRoleBiz.delete(Stream.of(roleIds.split(StringPool.COMMA))
                .map(id -> Long.parseLong(id)).collect(Collectors.toList()), user);
    }

}
