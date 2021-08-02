package com.ysy.demo.admin.system.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.enums.YesNoEnum;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.core.component.LockManagement;
import com.ysy.demo.admin.core.component.TransactionManagement;
import com.ysy.demo.admin.core.model.HandelResult;
import com.ysy.demo.admin.dto.BaseObjectRes;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.system.biz.SysUserBiz;
import com.ysy.demo.admin.system.dto.SysResultEntity;
import com.ysy.demo.admin.system.dto.UserAddReq;
import com.ysy.demo.admin.system.dto.UserDetailRes;
import com.ysy.demo.admin.system.dto.UserEditAvatarReq;
import com.ysy.demo.admin.system.dto.UserEditPasswordReq;
import com.ysy.demo.admin.system.dto.UserEditReq;
import com.ysy.demo.admin.system.dto.UserListReq;
import com.ysy.demo.admin.system.dto.UserListRes;
import com.ysy.demo.admin.system.dto.UserPasswordResetReq;
import com.ysy.demo.admin.system.entity.SysDept;
import com.ysy.demo.admin.system.entity.SysRole;
import com.ysy.demo.admin.system.entity.SysUser;
import com.ysy.demo.admin.system.entity.SysUserRole;
import com.ysy.demo.admin.system.service.MailService;
import com.ysy.demo.admin.system.service.SysDeptService;
import com.ysy.demo.admin.system.service.SysRoleService;
import com.ysy.demo.admin.system.service.SysUserRoleService;
import com.ysy.demo.admin.system.service.SysUserService;
import com.ysy.demo.admin.util.UserPasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysUserBizImpl implements SysUserBiz {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserPasswordUtils userPasswordUtils;

    @Autowired
    private LockManagement lockManagement;

    @Autowired
    private TransactionManagement transactionManagement;

    @Override
    public BasePageRes<UserListRes> pageList(UserListReq req, AuthUser user) {
        IPage<SysUser> page = new Page<>();
        if (req.getPage() != null) {
            page.setCurrent(req.getPage());
        }
        if (req.getPageSize() != null) {
            page.setSize(req.getPageSize());
        }
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDeleteFlag, YesNoEnum.NO.getValue()).orderByAsc(SysUser::getId);
        page = sysUserService.page(page, queryWrapper);
        List<UserListRes> records = page.getRecords().stream().map(e -> buildUserListRes(e, user)).collect(Collectors.toList());
        return BasePageRes.<UserListRes>builder().total(page.getTotal()).records(records).build();
    }

    @Override
    public UserDetailRes detail(Long id, AuthUser user) {
        SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getId, id)
                .eq(SysUser::getDeleteFlag, YesNoEnum.NO.getValue()).one();
        if (sysUser != null) {
            UserDetailRes res = new UserDetailRes();
            setRes(res, sysUser, user);
            List<Long> roleIds = sysUserRoleService.lambdaQuery().eq(SysUserRole::getDeleteFlag, YesNoEnum.NO.getValue())
                    .eq(SysUserRole::getUserId, id).list().stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            res.setRoleIds(roleIds);
            return res;
        }
        return null;
    }

    @Override
    public ResultEntity add(UserAddReq req, AuthUser user) {
        Integer roleLevel = verifyRoleAndGetMaxRoleLevel(req.getRoleIds(), user.getRoleLevel());
        String key = "user-" + req.getUsername();
        return lockManagement.handle(key, req, user, (req1, user1) -> {
            SysUser has = sysUserService.lambdaQuery().select(SysUser::getId).eq(SysUser::getUsername, req1.getUsername())
                    .eq(SysUser::getDeleteFlag, YesNoEnum.NO.getValue()).last("LIMIT 1").one();
            if (has != null) {
                return SysResultEntity.USERNAME_ALREADY_EXISTS;
            }
            SysUser saveUser = new SysUser();
            BeanUtils.copyProperties(req1, saveUser);
            String password = userPasswordUtils.generatePassword();
            saveUser.setPassword(userPasswordUtils.encryptOriginal(saveUser.getUsername(), password));
            saveUser.setRoleLevel(roleLevel);
            saveUser.setCreatorId(user1.getId());
            saveUser.setCreatorName(user1.getUsername());
            saveUser.setEditorId(user1.getId());
            saveUser.setEditorName(user1.getUsername());
            transactionManagement.handle(new Object[]{saveUser, req1, user1}, objects -> {
                SysUser u = (SysUser) objects[0];
                sysUserService.save(u);
                saveUserRole(u.getId(), ((UserAddReq) objects[1]).getRoleIds(), (AuthUser) objects[2]);
                return Boolean.TRUE;
            });
            HandelResult result = mailService.send("USER_ADD", saveUser.getEmail(), saveUser.getUsername(), password);
            if (!result.isSuccess()) {
                return SysResultEntity.USER_ADD_SEND_MAIL_FAILED;
            }
            return ResultEntity.SUCCESS;
        });
    }

    @Override
    public ResultEntity edit(UserEditReq req, AuthUser user) {
        if (!canEdit(req.getId(), user.getRoleLevel())) {
            return SysResultEntity.ROLE_PERMISSION_DENIED;
        }
        Integer roleLevel = verifyRoleAndGetMaxRoleLevel(req.getRoleIds(), user.getRoleLevel());
        String key = "user-" + req.getUsername();
        return lockManagement.handle(key, req, user, (req1, user1) -> {
            SysUser has = sysUserService.lambdaQuery().select(SysUser::getId).eq(SysUser::getUsername, req1.getUsername())
                    .ne(SysUser::getId, req1.getId()).eq(SysUser::getDeleteFlag, YesNoEnum.NO.getValue())
                    .last("LIMIT 1").one();
            if (has != null) {
                return SysResultEntity.USERNAME_ALREADY_EXISTS;
            }
            SysUser entity = new SysUser();
            BeanUtils.copyProperties(req1, entity);
            entity.setRoleLevel(roleLevel);
            entity.setEditorId(user1.getId());
            entity.setEditorName(user1.getUsername());
            sysUserService.lambdaUpdate().eq(SysUser::getId, req1.getId())
                    .eq(SysUser::getDeleteFlag, YesNoEnum.NO.getValue()).update(entity);
            updateUserRole(entity.getId(), req1.getRoleIds(), user1);
            return ResultEntity.SUCCESS;
        });
    }

    @Override
    public ResultEntity delete(List<Long> ids, AuthUser user) {
        if (!canEdit(ids, user.getRoleLevel())) {
            return SysResultEntity.ROLE_PERMISSION_DENIED;
        }
        sysUserService.lambdaUpdate().set(SysUser::getDeleteFlag, YesNoEnum.YES.getValue())
                .set(SysUser::getEditorId, user.getId()).set(SysUser::getEditorName, user.getUsername())
                .in(SysUser::getId, ids).eq(SysUser::getDeleteFlag, YesNoEnum.NO.getValue()).update();
        deleteUserRole(ids, user);
        return ResultEntity.SUCCESS;
    }

    @Override
    public ResultEntity resetPassword(UserPasswordResetReq req, AuthUser user) {
        List<SysUser> users = sysUserService.lambdaQuery().eq(SysUser::getDeleteFlag, YesNoEnum.NO.getValue())
                .gt(SysUser::getRoleLevel, user.getRoleLevel()).in(SysUser::getId, req.getIds()).list();
        if (users.size() != req.getIds().size()) {
            return SysResultEntity.ROLE_PERMISSION_DENIED;
        }
        List<Long> fieldUserIds = new ArrayList<>(users.size());
        List<Long> sendUserIds = new ArrayList<>(users.size());
        for (SysUser e : users) {
            String password = userPasswordUtils.generatePassword();
            String encryptPassword = userPasswordUtils.encryptOriginal(e.getUsername(), password);
            if (sysUserService.lambdaUpdate().set(SysUser::getPassword, encryptPassword)
                    .set(SysUser::getEditorId, user.getId()).set(SysUser::getEditorName, user.getUsername())
                    .eq(SysUser::getId, e.getId()).eq(SysUser::getDeleteFlag, YesNoEnum.NO.getValue()).update()) {
                if (!mailService.send("USER_PASSWORD_RESET", e.getEmail(), e.getUsername(), password).isSuccess()) {
                    sendUserIds.add(e.getId());
                }
            } else {
                fieldUserIds.add(e.getId());
            }
        }
        if (!fieldUserIds.isEmpty() || !sendUserIds.isEmpty()) {
            return ResultEntity.invalid("reset field ids=" + fieldUserIds + ", send email password failed ids="
                    + sendUserIds + ", please check user info or email!");
        }
        return ResultEntity.SUCCESS;
    }

    @Override
    public ResultEntity<BaseObjectRes<Boolean>> checkUsername(String username) {
        SysUser sysUser = sysUserService.getByUsername(username);
        return ResultEntity.success(new BaseObjectRes(sysUser == null));
    }

    @Override
    public ResultEntity updateAvatar(UserEditAvatarReq req, AuthUser user) {
        sysUserService.lambdaUpdate().set(SysUser::getAvatar, req.getAvatar())
                .eq(SysUser::getId, user.getId()).update();
        return ResultEntity.SUCCESS;
    }

    @Override
    public ResultEntity updatePassword(UserEditPasswordReq req, AuthUser user) {
        String password = userPasswordUtils.encrypt(user.getUsername(), req.getOldPassword());
        if (!user.getPassword().equals(password)) {
            return ResultEntity.invalid("Incorrect password");
        }
        password = userPasswordUtils.encrypt(user.getUsername(), req.getNewPassword());
        sysUserService.lambdaUpdate().set(SysUser::getPassword, password)
                .eq(SysUser::getId, user.getId()).update();
        return ResultEntity.SUCCESS;
    }

    private UserListRes buildUserListRes(SysUser sysUser, AuthUser operate) {
        UserListRes res = new UserListRes();
        setRes(res, sysUser, operate);
        return res;
    }

    private void setRes(UserListRes res, SysUser sysUser, AuthUser operate) {
        BeanUtils.copyProperties(sysUser, res);
        res.setLastLoginTime(sysUser.getLastLoginTime().getTime());
        res.setCreateTime(sysUser.getCreateTime().getTime());
        res.setUpdateTime(sysUser.getUpdateTime().getTime());
        SysDept dept = sysDeptService.getById(sysUser.getDeptId());
        res.setDeptName(dept == null ? "" : dept.getDeptName());
        res.setCanEdit(YesNoEnum.get(sysUser.getRoleLevel().intValue() > operate.getRoleLevel().intValue()).getValue());
    }

    private boolean canEdit(Long id, Integer roleLevel) {
        return sysUserService.lambdaQuery().eq(SysUser::getId, id).eq(SysUser::getDeleteFlag, YesNoEnum.NO.getValue())
                .gt(SysUser::getRoleLevel, roleLevel).count() == 1;
    }

    private boolean canEdit(List<Long> ids, Integer roleLevel) {
        return sysUserService.lambdaQuery().in(SysUser::getId, ids).eq(SysUser::getDeleteFlag, YesNoEnum.NO.getValue())
                .gt(SysUser::getRoleLevel, roleLevel).count() == ids.size();
    }

    private Integer verifyRoleAndGetMaxRoleLevel(List<Long> roleIds, Integer roleLevel) {
        List<SysRole> roles = sysRoleService.lambdaQuery().in(SysRole::getId, roleIds).gt(SysRole::getLevel, roleLevel)
                .eq(SysRole::getDeleteFlag, YesNoEnum.NO.getValue()).orderByAsc(SysRole::getLevel).list();
        if (roles.size() != roleIds.size()) {
            throw new IllegalArgumentException("Role param does not exist or insufficient permissions!");
        }
        return roles.get(0).getLevel();
    }

    private boolean updateUserRole(Long userId, List<Long> roleIds, AuthUser operator) {
        deleteUserRole(userId, operator);
        return saveUserRole(userId, roleIds, operator);
    }

    private boolean saveUserRole(Long userId, List<Long> roleIds, AuthUser operator) {
        return sysUserRoleService.saveBatch(roleIds.stream().map(e -> SysUserRole.builder().userId(userId).roleId(e)
                .creatorId(operator.getId()).creatorName(operator.getUsername()).editorId(operator.getId())
                .editorName(operator.getUsername()).build()).collect(Collectors.toList()));
    }

    private boolean deleteUserRole(Long userId, AuthUser operator) {
        return sysUserRoleService.lambdaUpdate().set(SysUserRole::getDeleteFlag, YesNoEnum.YES.getValue())
                .set(SysUserRole::getEditorId, operator.getId()).set(SysUserRole::getEditorName, operator.getUsername())
                .eq(SysUserRole::getUserId, userId).eq(SysUserRole::getDeleteFlag, YesNoEnum.NO.getValue()).update();
    }

    private boolean deleteUserRole(List<Long> userIds, AuthUser operator) {
        return sysUserRoleService.lambdaUpdate().set(SysUserRole::getDeleteFlag, YesNoEnum.YES.getValue())
                .set(SysUserRole::getEditorId, operator.getId()).set(SysUserRole::getEditorName, operator.getUsername())
                .in(SysUserRole::getUserId, userIds).eq(SysUserRole::getDeleteFlag, YesNoEnum.NO.getValue()).update();
    }
}
