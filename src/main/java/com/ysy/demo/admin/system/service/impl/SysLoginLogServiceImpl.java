package com.ysy.demo.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ysy.demo.admin.system.entity.SysLoginLog;
import com.ysy.demo.admin.system.mapper.SysLoginLogMapper;
import com.ysy.demo.admin.system.service.SysLoginLogService;
import org.springframework.stereotype.Service;

@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {
}
