package com.ysy.demo.admin.system.biz;

import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.system.dto.RoleListReq;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.system.dto.RoleAddReq;
import com.ysy.demo.admin.system.dto.RoleDetailRes;
import com.ysy.demo.admin.system.dto.RoleEditMenuReq;
import com.ysy.demo.admin.system.dto.RoleEditReq;
import com.ysy.demo.admin.system.dto.RoleListRes;

import java.util.List;

public interface SysRoleBiz {

    BasePageRes<RoleListRes> pageList(RoleListReq req, AuthUser user);

    RoleDetailRes detail(Long id, AuthUser user);

    ResultEntity add(RoleAddReq req, AuthUser user);

    ResultEntity edit(RoleEditReq req, AuthUser user);

    ResultEntity editMenu(RoleEditMenuReq req, AuthUser user);

    ResultEntity delete(List<Long> ids, AuthUser user);

}
