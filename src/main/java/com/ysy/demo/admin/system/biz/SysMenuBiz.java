package com.ysy.demo.admin.system.biz;

import com.ysy.demo.admin.dto.BaseTreeNodeRes;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.dto.BaseTreeRes;
import com.ysy.demo.admin.system.dto.MenuAddReq;
import com.ysy.demo.admin.system.dto.MenuEditReq;
import com.ysy.demo.admin.system.dto.MenuListReq;
import com.ysy.demo.admin.system.dto.MenuListRes;

import java.util.List;

public interface SysMenuBiz {

    BasePageRes<MenuListRes> pageList(MenuListReq req);

    ResultEntity add(MenuAddReq req, AuthUser user);

    ResultEntity edit(MenuEditReq req, AuthUser user);

    ResultEntity delete(List<Long> ids, AuthUser user);

    BaseTreeRes<BaseTreeNodeRes> tree();
}
