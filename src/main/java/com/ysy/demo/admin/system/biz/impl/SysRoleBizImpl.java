package com.ysy.demo.admin.system.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.enums.YesNoEnum;
import com.ysy.demo.admin.system.biz.SysRoleBiz;
import com.ysy.demo.admin.system.dto.RoleListReq;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.core.component.LockManagement;
import com.ysy.demo.admin.core.component.TransactionManagement;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.system.dto.RoleAddReq;
import com.ysy.demo.admin.system.dto.RoleDetailRes;
import com.ysy.demo.admin.system.dto.RoleEditMenuReq;
import com.ysy.demo.admin.system.dto.RoleEditReq;
import com.ysy.demo.admin.system.dto.RoleListRes;
import com.ysy.demo.admin.system.dto.SysResultEntity;
import com.ysy.demo.admin.system.entity.SysRole;
import com.ysy.demo.admin.system.entity.SysRoleMenu;
import com.ysy.demo.admin.system.entity.SysUserRole;
import com.ysy.demo.admin.system.service.SysRoleMenuService;
import com.ysy.demo.admin.system.service.SysRoleService;
import com.ysy.demo.admin.system.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysRoleBizImpl implements SysRoleBiz {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private LockManagement lockManagement;

    @Autowired
    private TransactionManagement transactionManagement;

    @Override
    public BasePageRes<RoleListRes> pageList(RoleListReq req, AuthUser user) {
        IPage<SysRole> page = new Page<>();
        if (req.getPage() != null) {
            page.setCurrent(req.getPage());
        }
        if (req.getPageSize() != null) {
            page.setSize(req.getPageSize());
        }
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getDeleteFlag, YesNoEnum.NO.getValue()).orderByAsc(SysRole::getId);
        page = sysRoleService.page(page, queryWrapper);
        List<RoleListRes> records = page.getRecords().stream().map(e -> buildRoleListRes(e, user)).collect(Collectors.toList());
        return BasePageRes.<RoleListRes>builder().total(page.getTotal()).records(records).build();
    }

    @Override
    public RoleDetailRes detail(Long id, AuthUser user) {
        SysRole entity = sysRoleService.lambdaQuery().eq(SysRole::getId, id)
                .eq(SysRole::getDeleteFlag, YesNoEnum.NO.getValue()).one();
        if (entity != null) {
            RoleDetailRes res = new RoleDetailRes();
            setRes(res, entity, user);
            List<Long> menuIds = sysRoleMenuService.lambdaQuery().eq(SysRoleMenu::getDeleteFlag, YesNoEnum.NO.getValue())
                    .eq(SysRoleMenu::getRoleId, id).list().stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
            res.setMenuIds(menuIds);
            return res;
        }
        return null;
    }

    @Override
    public ResultEntity add(RoleAddReq req, AuthUser user) {
        if (req.getLevel().intValue() <= user.getRoleLevel().intValue()) {
            return SysResultEntity.ROLE_PERMISSION_DENIED;
        }
        String key = "role-" + req.getRoleName();
        return lockManagement.handle(key, req, user, (req1, user1) -> {
            SysRole has = sysRoleService.lambdaQuery().select(SysRole::getId).eq(SysRole::getRoleName, req1.getRoleName())
                    .eq(SysRole::getDeleteFlag, YesNoEnum.NO.getValue()).last("LIMIT 1").one();
            if (has != null) {
                return SysResultEntity.ROLE_NAME_ALREADY_EXISTS;
            }
            SysRole role = new SysRole();
            BeanUtils.copyProperties(req1, role);
            role.setCreatorId(user1.getId());
            role.setCreatorName(user1.getUsername());
            role.setEditorId(user1.getId());
            role.setEditorName(user1.getUsername());
            transactionManagement.handle(new Object[]{role, req1, user1}, objects -> {
                SysRole role2 = (SysRole) objects[0];
                RoleAddReq req2 = (RoleAddReq) objects[1];
                sysRoleService.save(role2);
                if (!CollectionUtils.isEmpty(req2.getMenuIds())) {
                    saveRoleMenu(role2.getId(), req2.getMenuIds(), (AuthUser) objects[2]);
                }
                return Boolean.TRUE;
            });
            return ResultEntity.SUCCESS;
        });
    }

    @Override
    public ResultEntity edit(RoleEditReq req, AuthUser user) {
        if (req.getLevel().intValue() <= user.getRoleLevel().intValue()) {
            return SysResultEntity.ROLE_PERMISSION_DENIED;
        }
        if (!canEdit(req.getId(), user.getRoleLevel())) {
            return SysResultEntity.ROLE_PERMISSION_DENIED;
        }
        String key = "role-" + req.getRoleName();
        return lockManagement.handle(key, req, user, (req1, user1) -> {
            SysRole has = sysRoleService.lambdaQuery().select(SysRole::getId).eq(SysRole::getRoleName, req1.getRoleName())
                    .eq(SysRole::getDeleteFlag, YesNoEnum.NO.getValue()).ne(SysRole::getId, req1.getId())
                    .last("LIMIT 1").one();
            if (has != null) {
                return SysResultEntity.ROLE_NAME_ALREADY_EXISTS;
            }
            SysRole role = new SysRole();
            BeanUtils.copyProperties(req1, role);
            role.setEditorId(user1.getId());
            role.setEditorName(user1.getUsername());
            sysRoleService.lambdaUpdate().eq(SysRole::getId, req1.getId())
                    .eq(SysRole::getDeleteFlag, YesNoEnum.NO.getValue()).update(role);
            if (!CollectionUtils.isEmpty(req1.getMenuIds())) {
                updateRoleMenu(role.getId(), req1.getMenuIds(), user1);
            }
            return ResultEntity.SUCCESS;
        });
    }

    @Override
    public ResultEntity editMenu(RoleEditMenuReq req, AuthUser user) {
        if (!canEdit(req.getId(), user.getRoleLevel())) {
            return SysResultEntity.ROLE_PERMISSION_DENIED;
        }
        updateRoleMenu(req.getId(), req.getMenuIds(), user);
        return ResultEntity.SUCCESS;
    }

    @Override
    public ResultEntity delete(List<Long> ids, AuthUser user) {
        if (!canEdit(ids, user.getRoleLevel())) {
            return SysResultEntity.ROLE_PERMISSION_DENIED;
        }
        SysUserRole userRole = sysUserRoleService.lambdaQuery().select(SysUserRole::getId)
                .in(SysUserRole::getRoleId, ids).eq(SysUserRole::getDeleteFlag, YesNoEnum.NO.getValue())
                .last("LIMIT 1").one();
        if (userRole != null) {
            return SysResultEntity.ROLE_HAS_BEEN_USED;
        }
        sysRoleService.lambdaUpdate().set(SysRole::getDeleteFlag, YesNoEnum.YES.getValue())
                .set(SysRole::getEditorId, user.getId()).set(SysRole::getEditorName, user.getUsername())
                .in(SysRole::getId, ids).eq(SysRole::getDeleteFlag, YesNoEnum.NO.getValue()).update();
        return ResultEntity.SUCCESS;
    }

    private boolean canEdit(Long id, Integer roleLevel) {
        return sysRoleService.lambdaQuery().eq(SysRole::getId, id).eq(SysRole::getDeleteFlag, YesNoEnum.NO.getValue())
                .gt(SysRole::getLevel, roleLevel).count() == 1;
    }

    private boolean canEdit(List<Long> ids, Integer roleLevel) {
        return sysRoleService.lambdaQuery().in(SysRole::getId, ids).eq(SysRole::getDeleteFlag, YesNoEnum.NO.getValue())
                .gt(SysRole::getLevel, roleLevel).count() == ids.size();
    }

    private boolean updateRoleMenu(Long roleId, List<Long> menuIds, AuthUser operator) {
        sysRoleMenuService.lambdaUpdate().set(SysRoleMenu::getDeleteFlag, YesNoEnum.YES.getValue())
                .set(SysRoleMenu::getEditorId, operator.getId()).set(SysRoleMenu::getEditorName, operator.getUsername())
                .eq(SysRoleMenu::getDeleteFlag, YesNoEnum.NO.getValue()).eq(SysRoleMenu::getRoleId, roleId).update();
        return saveRoleMenu(roleId, menuIds, operator);
    }

    private boolean saveRoleMenu(Long roleId, List<Long> menuIds, AuthUser operator) {
        return sysRoleMenuService.saveBatch(menuIds.stream().map(e -> SysRoleMenu.builder().roleId(roleId).menuId(e)
                .creatorId(operator.getId()).creatorName(operator.getUsername()).editorId(operator.getId())
                .editorName(operator.getUsername()).build()).collect(Collectors.toList()));
    }

    private RoleListRes buildRoleListRes(SysRole role, AuthUser user) {
        RoleListRes res = new RoleListRes();
        setRes(res, role, user);
        return res;
    }

    private void setRes(RoleListRes res, SysRole role, AuthUser user) {
        BeanUtils.copyProperties(role, res);
        res.setCreateTime(role.getCreateTime().getTime());
        res.setUpdateTime(role.getUpdateTime().getTime());
        res.setCanEdit(YesNoEnum.get(role.getLevel().intValue() > user.getRoleLevel().intValue()).getValue());
    }
}
