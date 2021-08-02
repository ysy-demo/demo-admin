package com.ysy.demo.admin.system.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ysy.demo.admin.dto.BaseTreeNodeRes;
import com.ysy.demo.admin.enums.YesNoEnum;
import com.ysy.demo.admin.util.TreeUtils;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.core.component.LockManagement;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.dto.BaseTreeRes;
import com.ysy.demo.admin.system.biz.SysMenuBiz;
import com.ysy.demo.admin.system.dto.MenuAddReq;
import com.ysy.demo.admin.system.dto.MenuEditReq;
import com.ysy.demo.admin.system.dto.MenuListReq;
import com.ysy.demo.admin.system.dto.MenuListRes;
import com.ysy.demo.admin.system.dto.SysResultEntity;
import com.ysy.demo.admin.system.entity.SysMenu;
import com.ysy.demo.admin.system.enums.SysMenuType;
import com.ysy.demo.admin.system.service.SysMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuBizImpl implements SysMenuBiz {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private LockManagement lockManagement;

    @Override
    public BasePageRes<MenuListRes> pageList(MenuListReq req) {
        IPage<SysMenu> page = new Page<>();
        if (req.getPage() != null) {
            page.setCurrent(req.getPage());
        }
        if (req.getPageSize() != null) {
            page.setSize(req.getPageSize());
        }
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getDeleteFlag, YesNoEnum.NO.getValue()).orderByAsc(SysMenu::getId);
        page = sysMenuService.page(page, queryWrapper);
        List<MenuListRes> records = page.getRecords().stream().map(e -> buildMenuListRes(e)).collect(Collectors.toList());
        return BasePageRes.<MenuListRes>builder().total(page.getTotal()).records(records).build();
    }

    @Override
    public ResultEntity add(MenuAddReq req, AuthUser user) {
        saveVerify(req);
        String key = "menu-" + req.getParentId() + "-" + req.getMenuName();
        return lockManagement.handle(key, req, user, (req1, user1) -> {
            SysMenu has = sysMenuService.lambdaQuery().select(SysMenu::getId).eq(SysMenu::getParentId, req1.getParentId())
                    .eq(SysMenu::getMenuName, req1.getMenuName()).eq(SysMenu::getDeleteFlag, YesNoEnum.NO.getValue())
                    .last("LIMIT 1").one();
            if (has != null) {
                return SysResultEntity.MENU_NAME_ALREADY_EXISTS;
            }
            SysMenu menu = new SysMenu();
            BeanUtils.copyProperties(req1, menu);
            menu.setCreatorId(user1.getId());
            menu.setCreatorName(user1.getUsername());
            menu.setEditorId(user1.getId());
            menu.setEditorName(user1.getUsername());
            sysMenuService.save(menu);
            return ResultEntity.SUCCESS;
        });
    }

    @Override
    public ResultEntity edit(MenuEditReq req, AuthUser user) {
        saveVerify(req);
        String key = "menu-" + req.getParentId() + "-" + req.getMenuName();
        return lockManagement.handle(key, req, user, (req1, user1) -> {
            SysMenu has = sysMenuService.lambdaQuery().select(SysMenu::getId).eq(SysMenu::getParentId, req1.getParentId())
                    .eq(SysMenu::getMenuName, req1.getMenuName()).ne(SysMenu::getId, req1.getId())
                    .eq(SysMenu::getDeleteFlag, YesNoEnum.NO.getValue()).last("LIMIT 1").one();
            if (has != null) {
                return SysResultEntity.MENU_NAME_ALREADY_EXISTS;
            }
            SysMenu menu = new SysMenu();
            BeanUtils.copyProperties(req1, menu);
            menu.setEditorId(user1.getId());
            menu.setEditorName(user1.getUsername());
            sysMenuService.lambdaUpdate().eq(SysMenu::getId, req1.getId())
                    .eq(SysMenu::getDeleteFlag, YesNoEnum.NO.getValue()).update(menu);
            return ResultEntity.SUCCESS;
        });
    }

    @Override
    public ResultEntity delete(List<Long> ids, AuthUser user) {
        sysMenuService.lambdaUpdate().set(SysMenu::getDeleteFlag, YesNoEnum.YES.getValue())
                .set(SysMenu::getEditorId, user.getId()).set(SysMenu::getEditorName, user.getUsername())
                .in(SysMenu::getId, ids).eq(SysMenu::getDeleteFlag, YesNoEnum.NO.getValue()).update();
        return ResultEntity.SUCCESS;
    }

    @Override
    public BaseTreeRes<BaseTreeNodeRes> tree() {
        List<SysMenu> data = sysMenuService.lambdaQuery().eq(SysMenu::getDeleteFlag, YesNoEnum.NO.getValue())
                .orderByAsc(SysMenu::getSort, SysMenu::getId).list();
        return TreeUtils.build(data, e -> BaseTreeNodeRes.builder().id(e.getId())
                .pid(e.getParentId()).name(e.getMenuName()).build());
    }

    private void saveVerify(MenuAddReq req) {
        if (SysMenuType.BUTTON.getValue().equals(req.getType()) && req.getParentId() == 0) {
            throw new IllegalArgumentException("parentId is null");
        }
    }

    private MenuListRes buildMenuListRes(SysMenu e) {
        MenuListRes res = new MenuListRes();
        BeanUtils.copyProperties(e, res);
        res.setCreateTime(e.getCreateTime().getTime());
        res.setUpdateTime(e.getUpdateTime().getTime());
        return res;
    }

}
