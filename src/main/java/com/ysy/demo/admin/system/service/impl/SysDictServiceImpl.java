package com.ysy.demo.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ysy.demo.admin.system.entity.SysDict;
import com.ysy.demo.admin.system.mapper.SysDictMapper;
import com.ysy.demo.admin.system.service.SysDictService;
import org.springframework.stereotype.Service;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {
}
