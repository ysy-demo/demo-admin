package com.ysy.demo.admin.system.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.core.component.LockManagement;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.enums.YesNoEnum;
import com.ysy.demo.admin.system.biz.SysDictBiz;
import com.ysy.demo.admin.system.dto.DictEditReq;
import com.ysy.demo.admin.system.dto.DictListReq;
import com.ysy.demo.admin.system.dto.DictRes;
import com.ysy.demo.admin.system.dto.SysResultEntity;
import com.ysy.demo.admin.system.service.SysDictService;
import com.google.common.collect.Maps;
import com.ysy.demo.admin.system.dto.DictAddReq;
import com.ysy.demo.admin.system.dto.DictListRes;
import com.ysy.demo.admin.system.entity.SysDict;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysDictBizImpl implements SysDictBiz {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private LockManagement lockManagement;

    @Override
    public BasePageRes<DictListRes> pageList(DictListReq req) {
        IPage<SysDict> page = new Page<>();
        if (req.getPage() != null) {
            page.setCurrent(req.getPage());
        }
        if (req.getPageSize() != null) {
            page.setSize(req.getPageSize());
        }
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getDeleteFlag, YesNoEnum.NO.getValue()).orderByAsc(SysDict::getId);
        page = sysDictService.page(page, queryWrapper);
        List<DictListRes> records = page.getRecords().stream().map(e -> buildListRes(e)).collect(Collectors.toList());
        return BasePageRes.<DictListRes>builder().total(page.getTotal()).records(records).build();
    }


    @Override
    public ResultEntity add(DictAddReq req, AuthUser user) {
        String key = "dict-" + req.getType() + "-" + req.getCode();
        return lockManagement.handle(key, req, user, (req1, user1) -> {
            SysDict has = sysDictService.lambdaQuery().select(SysDict::getId).eq(SysDict::getType, req1.getType())
                    .eq(SysDict::getCode, req1.getCode()).eq(SysDict::getDeleteFlag, YesNoEnum.NO.getValue())
                    .last("LIMIT 1").one();
            if (has != null) {
                return SysResultEntity.DICT_CODE_ALREADY_EXISTS;
            }
            SysDict entity = new SysDict();
            BeanUtils.copyProperties(req1, entity);
            entity.setCreatorId(user1.getId());
            entity.setCreatorName(user1.getUsername());
            entity.setEditorId(user1.getId());
            entity.setEditorName(user1.getUsername());
            sysDictService.save(entity);
            return ResultEntity.SUCCESS;
        });
    }

    @Override
    public ResultEntity edit(DictEditReq req, AuthUser user) {
        String key = "dict-" + req.getType() + "-" + req.getCode();
        return lockManagement.handle(key, req, user, (req1, user1) -> {
            SysDict has = sysDictService.lambdaQuery().select(SysDict::getId).eq(SysDict::getType, req1.getType())
                    .eq(SysDict::getCode, req1.getCode()).ne(SysDict::getId, req1.getId())
                    .eq(SysDict::getDeleteFlag, YesNoEnum.NO.getValue()).last("LIMIT 1").one();
            if (has != null) {
                return SysResultEntity.DICT_CODE_ALREADY_EXISTS;
            }
            SysDict entity = new SysDict();
            BeanUtils.copyProperties(req1, entity);
            entity.setEditorId(user1.getId());
            entity.setEditorName(user1.getUsername());
            sysDictService.lambdaUpdate().eq(SysDict::getId, req1.getId())
                    .eq(SysDict::getDeleteFlag, YesNoEnum.NO.getValue()).update(entity);
            return ResultEntity.SUCCESS;
        });
    }

    @Override
    public ResultEntity delete(List<Long> ids, AuthUser user) {
        sysDictService.lambdaUpdate().set(SysDict::getDeleteFlag, YesNoEnum.YES.getValue())
                .set(SysDict::getEditorId, user.getId()).set(SysDict::getEditorName, user.getUsername())
                .in(SysDict::getId, ids).eq(SysDict::getDeleteFlag, YesNoEnum.NO.getValue()).update();
        return ResultEntity.SUCCESS;
    }

    @Override
    public List<DictRes> getDict(String type) {
        return sysDictService.lambdaQuery().eq(SysDict::getType, type).eq(SysDict::getDeleteFlag, YesNoEnum.NO.getValue())
                .orderByAsc(SysDict::getSort, SysDict::getId).list().stream().map(e -> new DictRes(e.getCode(), e.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<DictRes>> getDictMap(List<String> types) {
        Map<String, List<DictRes>> map = Maps.newHashMapWithExpectedSize(types.size());
        for (String type : types) {
            map.put(type, getDict(type));
        }
        return map;
    }

    private DictListRes buildListRes(SysDict e) {
        DictListRes res = new DictListRes();
        BeanUtils.copyProperties(e, res);
        res.setCreateTime(e.getCreateTime().getTime());
        res.setUpdateTime(e.getUpdateTime().getTime());
        return res;
    }
}
