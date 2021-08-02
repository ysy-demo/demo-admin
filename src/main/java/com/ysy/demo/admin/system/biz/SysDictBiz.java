package com.ysy.demo.admin.system.biz;

import com.ysy.demo.admin.core.auth.entity.AuthUser;
import com.ysy.demo.admin.core.model.ResultEntity;
import com.ysy.demo.admin.dto.BasePageRes;
import com.ysy.demo.admin.system.dto.DictEditReq;
import com.ysy.demo.admin.system.dto.DictRes;
import com.ysy.demo.admin.system.dto.DictAddReq;
import com.ysy.demo.admin.system.dto.DictListReq;
import com.ysy.demo.admin.system.dto.DictListRes;

import java.util.List;
import java.util.Map;

public interface SysDictBiz {

    BasePageRes<DictListRes> pageList(DictListReq req);

    ResultEntity add(DictAddReq req, AuthUser user);

    ResultEntity edit(DictEditReq req, AuthUser user);

    ResultEntity delete(List<Long> ids, AuthUser user);

    List<DictRes> getDict(String type);

    Map<String, List<DictRes>> getDictMap(List<String> types);
}
