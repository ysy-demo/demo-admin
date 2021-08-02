package com.ysy.demo.admin.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.dto.BaseTreeNodeRes;
import com.ysy.demo.admin.system.biz.SysMenuBiz;
import com.ysy.demo.admin.system.dto.MenuAddReq;
import com.ysy.demo.admin.system.dto.MenuEditReq;
import com.ysy.demo.admin.system.dto.MenuListReq;
import com.ysy.demo.admin.system.dto.MenuListRes;
import com.ysy.demo.admin.core.annotation.Log;
import com.ysy.demo.admin.core.auth.util.AuthInfoUtils;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.dto.BaseTreeRes;
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

@Api(tags = "菜单管理接口")
@RestController
@RequestMapping("sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuBiz sysMenuBiz;

    @GetMapping("tree")
    public ResultEntity<BaseTreeRes<BaseTreeNodeRes>> tree() {
        BaseTreeRes<BaseTreeNodeRes> res = sysMenuBiz.tree();
        return ResultEntity.success(res);
    }

    @ApiOperation("列表：权限 sys_menu:list")
    @GetMapping
    @RequiresPermissions("sys_menu:list")
    public ResultEntity<BasePageRes<MenuListRes>> list(@Valid MenuListReq req) {
        BasePageRes<MenuListRes> res = sysMenuBiz.pageList(req);
        return ResultEntity.success(res);
    }

    @ApiOperation("添加：权限 sys_menu:add")
    @Log("新增菜单/按钮")
    @PostMapping
    @RequiresPermissions("sys_menu:add")
    public ResultEntity add(@Valid @RequestBody MenuAddReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysMenuBiz.add(req, user);
    }

    @ApiOperation("编辑：权限 sys_menu:edit")
    @Log("修改菜单/按钮")
    @PutMapping
    @RequiresPermissions("sys_menu:edit")
    public ResultEntity edit(@Valid @RequestBody MenuEditReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysMenuBiz.edit(req, user);
    }

    @ApiOperation("删除：权限 sys_menu:delete")
    @Log("删除菜单/按钮")
    @DeleteMapping("{menuIds}")
    @RequiresPermissions("sys_menu:delete")
    public ResultEntity delete(@NotBlank(message = "{required}") @PathVariable String menuIds) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysMenuBiz.delete(Stream.of(menuIds.split(StringPool.COMMA))
                .map(id -> Long.parseLong(id)).collect(Collectors.toList()), user);
    }

}
