package com.ysy.demo.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ysy.demo.admin.system.entity.SysLog;
import com.ysy.demo.admin.system.mapper.SysLogMapper;
import com.ysy.demo.admin.system.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {
}
