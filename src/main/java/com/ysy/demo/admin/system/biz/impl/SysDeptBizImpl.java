package com.ysy.demo.admin.system.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.dto.BaseTreeNodeRes;
import com.ysy.demo.admin.enums.YesNoEnum;
import com.ysy.demo.admin.system.biz.SysDeptBiz;
import com.ysy.demo.admin.system.dto.DeptListReq;
import com.ysy.demo.admin.system.dto.DeptListRes;
import com.ysy.demo.admin.system.dto.SysResultEntity;
import com.ysy.demo.admin.util.TreeUtils;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.core.component.LockManagement;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.dto.BaseTreeRes;
import com.ysy.demo.admin.system.dto.DeptAddReq;
import com.ysy.demo.admin.system.dto.DeptEditReq;
import com.ysy.demo.admin.system.entity.SysDept;
import com.ysy.demo.admin.system.entity.SysUser;
import com.ysy.demo.admin.system.service.SysDeptService;
import com.ysy.demo.admin.system.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysDeptBizImpl implements SysDeptBiz {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private LockManagement lockManagement;

    @Override
    public BasePageRes<DeptListRes> pageList(DeptListReq req) {
        IPage<SysDept> page = new Page<>();
        if (req.getPage() != null) {
            page.setCurrent(req.getPage());
        }
        if (req.getPageSize() != null) {
            page.setSize(req.getPageSize());
        }
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getDeleteFlag, YesNoEnum.NO.getValue()).orderByAsc(SysDept::getId);
        queryWrapper.like(!StringUtils.isEmpty(req.getDeptName()), SysDept::getDeptName, req.getDeptName());
        queryWrapper.eq(req.getParentId() != null, SysDept::getParentId, req.getParentId());
        page = sysDeptService.page(page, queryWrapper);
        List<DeptListRes> records = page.getRecords().stream().map(e -> buildDeptListRes(e)).collect(Collectors.toList());
        return BasePageRes.<DeptListRes>builder().total(page.getTotal()).records(records).build();
    }

    @Override
    public BaseTreeRes<BaseTreeNodeRes> tree() {
        List<SysDept> data = sysDeptService.lambdaQuery().eq(SysDept::getDeleteFlag, YesNoEnum.NO.getValue())
                .orderByAsc(SysDept::getSort, SysDept::getId).list();
        return TreeUtils.build(data, e -> BaseTreeNodeRes.builder().id(e.getId())
                .pid(e.getParentId()).name(e.getDeptName()).build());
    }

    @Override
    public ResultEntity add(DeptAddReq req, AuthUser user) {
        String key = "dept-" + req.getParentId() + "-" + req.getDeptName();
        return lockManagement.handle(key, req, user, (req1, user1) -> {
            SysDept has = sysDeptService.lambdaQuery().select(SysDept::getId).eq(SysDept::getParentId, req1.getParentId())
                    .eq(SysDept::getDeptName, req1.getDeptName()).eq(SysDept::getDeleteFlag, YesNoEnum.NO.getValue())
                    .last("LIMIT 1").one();
            if (has != null) {
                return SysResultEntity.DEPT_NAME_ALREADY_EXISTS;
            }
            SysDept saveDept = new SysDept();
            BeanUtils.copyProperties(req1, saveDept);
            saveDept.setCreatorId(user1.getId());
            saveDept.setCreatorName(user1.getUsername());
            saveDept.setEditorId(user1.getId());
            saveDept.setEditorName(user1.getUsername());
            sysDeptService.save(saveDept);
            return ResultEntity.SUCCESS;
        });
    }

    @Override
    public ResultEntity edit(DeptEditReq req, AuthUser user) {
        String key = "dept-" + req.getParentId() + "-" + req.getDeptName();
        return lockManagement.handle(key, req, user, (req1, user1) -> {
            SysDept has = sysDeptService.lambdaQuery().select(SysDept::getId).eq(SysDept::getParentId, req1.getParentId())
                    .eq(SysDept::getDeptName, req1.getDeptName()).ne(SysDept::getId, req1.getId())
                    .eq(SysDept::getDeleteFlag, YesNoEnum.NO.getValue()).last("LIMIT 1").one();
            if (has != null) {
                return SysResultEntity.DEPT_NAME_ALREADY_EXISTS;
            }
            SysDept dept = new SysDept();
            BeanUtils.copyProperties(req1, dept);
            dept.setEditorId(user1.getId());
            dept.setEditorName(user1.getUsername());
            sysDeptService.lambdaUpdate().eq(SysDept::getId, req1.getId())
                    .eq(SysDept::getDeleteFlag, YesNoEnum.NO.getValue()).update(dept);
            return ResultEntity.SUCCESS;
        });
    }

    @Override
    public ResultEntity delete(List<Long> ids, AuthUser user) {
        SysUser hasUsed = sysUserService.lambdaQuery().select(SysUser::getId).in(SysUser::getDeptId, ids)
                .eq(SysUser::getDeleteFlag, YesNoEnum.NO.getValue()).last("LIMIT 1").one();
        if (hasUsed != null) {
            return SysResultEntity.DEPT_HAS_BEEN_USED;
        }
        sysDeptService.lambdaUpdate().set(SysDept::getDeleteFlag, YesNoEnum.YES.getValue())
                .set(SysDept::getEditorId, user.getId()).set(SysDept::getEditorName, user.getUsername())
                .in(SysDept::getId, ids).eq(SysDept::getDeleteFlag, YesNoEnum.NO.getValue()).update();
        return ResultEntity.SUCCESS;
    }

    private DeptListRes buildDeptListRes(SysDept dept) {
        DeptListRes res = new DeptListRes();
        BeanUtils.copyProperties(dept, res);
        res.setCreateTime(dept.getCreateTime().getTime());
        res.setUpdateTime(dept.getUpdateTime().getTime());
        return res;
    }

}
