package com.ysy.demo.admin.system.biz;

import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.dto.BaseTreeNodeRes;
import com.ysy.demo.admin.system.dto.DeptListReq;
import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.dto.BaseTreeRes;
import com.ysy.demo.admin.system.dto.DeptAddReq;
import com.ysy.demo.admin.system.dto.DeptEditReq;
import com.ysy.demo.admin.system.dto.DeptListRes;

import java.util.List;

public interface SysDeptBiz {

    BasePageRes<DeptListRes> pageList(DeptListReq req);

    BaseTreeRes<BaseTreeNodeRes> tree();

    ResultEntity add(DeptAddReq req, AuthUser user);

    ResultEntity edit(DeptEditReq req, AuthUser user);

    ResultEntity delete(List<Long> ids, AuthUser user);

}
