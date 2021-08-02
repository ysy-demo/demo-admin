package com.ysy.demo.admin.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.dto.BaseTreeNodeRes;
import com.ysy.demo.admin.dto.BaseTreeRes;
import com.ysy.demo.admin.system.biz.SysDeptBiz;
import com.ysy.demo.admin.system.dto.DeptAddReq;
import com.ysy.demo.admin.system.dto.DeptEditReq;
import com.ysy.demo.admin.system.dto.DeptListReq;
import com.ysy.demo.admin.system.dto.DeptListRes;
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

@Api(tags = "部门管理接口")
@RestController
@RequestMapping("sys/dept")
public class SysDeptController {

    @Autowired
    private SysDeptBiz sysDeptBiz;

    @ApiOperation("列表：权限 sys_dept:list")
    @GetMapping
    @RequiresPermissions("sys_dept:list")
    public ResultEntity<BasePageRes<DeptListRes>> list(@Valid DeptListReq req) {
        BasePageRes<DeptListRes> res = sysDeptBiz.pageList(req);
        return ResultEntity.success(res);
    }

    @GetMapping("tree")
    public ResultEntity<BaseTreeRes<BaseTreeNodeRes>> tree() {
        BaseTreeRes<BaseTreeNodeRes> res = sysDeptBiz.tree();
        return ResultEntity.success(res);
    }

    @ApiOperation("新增：权限 sys_dept:add")
    @Log("新增部门")
    @PostMapping
    @RequiresPermissions("sys_dept:add")
    public ResultEntity add(@Valid @RequestBody DeptAddReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysDeptBiz.add(req, user);
    }

    @ApiOperation("编辑：权限 sys_dept:edit")
    @Log("修改部门")
    @PutMapping
    @RequiresPermissions("sys_dept:edit")
    public ResultEntity edit(@Valid @RequestBody DeptEditReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysDeptBiz.edit(req, user);
    }

    @ApiOperation("删除：权限 sys_dept:delete")
    @Log("删除部门")
    @DeleteMapping("{ids}")
    @RequiresPermissions("sys_dept:delete")
    public ResultEntity delete(@NotBlank(message = "{required}") @PathVariable String ids) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysDeptBiz.delete(Stream.of(ids.split(StringPool.COMMA))
                .map(id -> Long.parseLong(id)).collect(Collectors.toList()), user);
    }

}
