package com.ysy.demo.admin.system.biz;

import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.dto.BaseObjectRes;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.system.dto.UserAddReq;
import com.ysy.demo.admin.system.dto.UserDetailRes;
import com.ysy.demo.admin.system.dto.UserEditAvatarReq;
import com.ysy.demo.admin.system.dto.UserEditPasswordReq;
import com.ysy.demo.admin.system.dto.UserEditReq;
import com.ysy.demo.admin.system.dto.UserListReq;
import com.ysy.demo.admin.system.dto.UserListRes;
import com.ysy.demo.admin.system.dto.UserPasswordResetReq;

import java.util.List;

public interface SysUserBiz {

    BasePageRes<UserListRes> pageList(UserListReq req, AuthUser user);

    UserDetailRes detail(Long id, AuthUser user);

    ResultEntity add(UserAddReq req, AuthUser user);

    ResultEntity edit(UserEditReq req, AuthUser user);

    ResultEntity delete(List<Long> ids, AuthUser user);

    ResultEntity resetPassword(UserPasswordResetReq req, AuthUser user);

    ResultEntity<BaseObjectRes<Boolean>> checkUsername(String username);

    ResultEntity updateAvatar(UserEditAvatarReq req, AuthUser user);

    ResultEntity updatePassword(UserEditPasswordReq req, AuthUser user);

}
