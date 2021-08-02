package com.ysy.demo.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ysy.demo.admin.enums.YesNoEnum;
import com.ysy.demo.admin.system.entity.SysUser;
import com.ysy.demo.admin.system.mapper.SysUserMapper;
import com.ysy.demo.admin.system.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser getByUsername(String username) {
        return lambdaQuery().eq(SysUser::getUsername, username)
                .eq(SysUser::getDeleteFlag, YesNoEnum.NO.getValue()).one();
    }
}
