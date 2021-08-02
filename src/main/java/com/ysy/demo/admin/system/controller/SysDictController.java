package com.ysy.demo.admin.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.core.auth.util.AuthInfoUtils;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.dto.BaseListRes;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.system.biz.SysDictBiz;
import com.ysy.demo.admin.system.dto.DictEditReq;
import com.ysy.demo.admin.system.dto.DictRes;
import com.ysy.demo.admin.core.annotation.Log;
import com.ysy.demo.admin.system.dto.DictAddReq;
import com.ysy.demo.admin.system.dto.DictListReq;
import com.ysy.demo.admin.system.dto.DictListRes;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Api(tags = "字典管理接口")
@RestController
@RequestMapping("sys/dict")
public class SysDictController {

    @Autowired
    private SysDictBiz sysDictBiz;

    @ApiOperation("列表：权限 sys_dict:list")
    @GetMapping
    @RequiresPermissions("sys_dict:list")
    public ResultEntity<BasePageRes<DictListRes>> list(@Valid DictListReq req) {
        BasePageRes<DictListRes> res = sysDictBiz.pageList(req);
        return ResultEntity.success(res);
    }

    @ApiOperation("新增：权限 sys_dict:add")
    @Log("新增字典")
    @PostMapping
    @RequiresPermissions("sys_dict:add")
    public ResultEntity add(@Valid @RequestBody DictAddReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysDictBiz.add(req, user);
    }

    @ApiOperation("编辑：权限 sys_dict:edit")
    @Log("修改字典")
    @PutMapping
    @RequiresPermissions("sys_dict:edit")
    public ResultEntity edit(@Valid @RequestBody DictEditReq req) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysDictBiz.edit(req, user);
    }

    @ApiOperation("删除：权限 sys_dict:delete")
    @Log("删除字典")
    @DeleteMapping("{ids}")
    @RequiresPermissions("sys_dict:delete")
    public ResultEntity delete(@NotBlank(message = "{required}") @PathVariable String ids) {
        AuthUser user = AuthInfoUtils.getUser();
        return sysDictBiz.delete(Stream.of(ids.split(StringPool.COMMA))
                .map(id -> Long.parseLong(id)).collect(Collectors.toList()), user);
    }

    @ApiOperation("获取字典")
    @GetMapping("type/{type}")
    public ResultEntity<BaseListRes<DictRes>> type(@NotBlank(message = "{required}") @PathVariable String type) {
        List<DictRes> list = sysDictBiz.getDict(type);
        return ResultEntity.success(BaseListRes.<DictRes>builder().rows(list).build());
    }

    @ApiOperation("获取字典")
    @GetMapping("type/map/{types}")
    public ResultEntity<Map<String, List<DictRes>>> typeMap(@NotBlank(message = "{required}") @PathVariable String types) {
        Map<String, List<DictRes>> map = sysDictBiz.getDictMap(Arrays.asList(types.split(StringPool.COMMA)));
        return ResultEntity.success(map);
    }
}
