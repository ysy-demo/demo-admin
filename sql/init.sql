DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '上级Id',
  `dept_name` varchar(30) NOT NULL DEFAULT '' COMMENT '名称',
  `sort` double(8,2) NOT NULL DEFAULT 0.00 COMMENT '排序',
  `creator_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建者Id',
  `creator_name` varchar(30) NOT NULL DEFAULT '' COMMENT '创建者名称',
  `editor_id` int(11) NOT NULL DEFAULT 0 COMMENT '修改者id',
  `editor_name` varchar(30) NOT NULL DEFAULT '' COMMENT '修改者名称',
  `delete_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标注：0-否，1-是',
  `create_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) ON UPDATE current_timestamp(3) COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统部门表';

DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `type` varchar(30) NOT NULL COMMENT '类型',
  `code` varchar(30) NOT NULL COMMENT '编码',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `sort` double(8,2) NOT NULL DEFAULT 0.00 COMMENT '排序',
  `creator_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建者Id',
  `creator_name` varchar(30) NOT NULL DEFAULT '' COMMENT '创建者名称',
  `editor_id` int(11) NOT NULL DEFAULT 0 COMMENT '修改者id',
  `editor_name` varchar(30) NOT NULL DEFAULT '' COMMENT '修改者名称',
  `delete_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标注：0-否，1-是',
  `create_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) ON UPDATE current_timestamp(3) COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统字典表';

INSERT INTO `sys_dict` (`type`, `code`, `name`, `sort`) VALUES ('roleLevel', '1', '超级', '1');
INSERT INTO `sys_dict` (`type`, `code`, `name`, `sort`) VALUES ('roleLevel', '2', '管理员', '2');
INSERT INTO `sys_dict` (`type`, `code`, `name`, `sort`) VALUES ('roleLevel', '3', '普通', '3');
INSERT INTO `sys_dict` (`type`, `code`, `name`, `sort`) VALUES ('menuType', '0', '菜单', '1');
INSERT INTO `sys_dict` (`type`, `code`, `name`, `sort`) VALUES ('menuType', '1', '按钮', '2');

DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `user_id` int(11) NOT NULL DEFAULT 0 COMMENT '用户Id',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '操作用户',
  `operation` varchar(30) NOT NULL DEFAULT '' COMMENT '操作内容',
  `time` bigint(20) NOT NULL DEFAULT 0 COMMENT '耗时',
  `method` varchar(100) NOT NULL DEFAULT '' COMMENT '操作方法',
  `params` text NOT NULL COMMENT '方法参数',
  `result` text NOT NULL COMMENT '返回结果',
  `ip` varchar(64) NOT NULL DEFAULT '' COMMENT '操作者IP',
  `create_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';

DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `ip` varchar(64) NOT NULL DEFAULT '' COMMENT 'IP地址',
  `create_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户登录日志';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '上级菜单id',
  `menu_name` varchar(30) NOT NULL DEFAULT '' COMMENT '菜单/按钮名称',
  `perms` varchar(50) NOT NULL DEFAULT '' COMMENT '权限标识',
  `type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '类型：0-菜单，1-按钮',
  `sort` double(8,2) NOT NULL DEFAULT 0.00 COMMENT '排序',
  `creator_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建者Id',
  `creator_name` varchar(30) NOT NULL DEFAULT '' COMMENT '创建者名称',
  `editor_id` int(11) NOT NULL DEFAULT 0 COMMENT '修改者id',
  `editor_name` varchar(30) NOT NULL DEFAULT '' COMMENT '修改者名称',
  `delete_flag` tinyint(255) NOT NULL DEFAULT 0 COMMENT '删除标注：0-否，1-是',
  `create_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) ON UPDATE current_timestamp(3) COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('1', '0', '菜单列表', 'sys_menu:list', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('2', '1', '菜单添加', 'sys_menu:add', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('3', '1', '菜单删除', 'sys_menu:delete', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('4', '1', '菜单编辑', 'sys_menu:edit', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('5', '0', '部门列表', 'sys_dept:list', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('6', '5', '部门添加', 'sys_dept:add', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('7', '5', '部门编辑', 'sys_dept:edit', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('8', '5', '部门删除', 'sys_dept:delete', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('9', '0', '角色列表', 'sys_role:list', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('10', '9', '角色添加', 'sys_role:add', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('11', '9', '角色编辑', 'sys_role:edit', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('12', '9', '角色删除', 'sys_role:delete', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('13', '0', '用户列表', 'sys_user:list', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('14', '13', '用户添加', 'sys_user:add', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('15', '13', '用户编辑', 'sys_user:edit', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('16', '13', '用户删除', 'sys_user:delete', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('17', '13', '重置密码', 'sys_user:password-reset', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('18', '0', '字典列表', 'sys_dict:list', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('19', '18', '字典添加', 'sys_dict:add', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('20', '18', '字典编辑', 'sys_dict:edit', '1');
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `perms`, `type`) VALUES ('21', '18', '字典删除', 'sys_dict:delete', '1');

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `role_code` varchar(20) NOT NULL DEFAULT '' COMMENT '角色编码',
  `role_name` varchar(30) NOT NULL DEFAULT '' COMMENT '角色名称',
  `level` tinyint(1) NOT NULL DEFAULT 3 COMMENT '级别：1-超级，2-管理员，3-普通',
  `remark` varchar(100) NOT NULL DEFAULT '' COMMENT '角色描述',
  `creator_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建者Id',
  `creator_name` varchar(30) NOT NULL DEFAULT '' COMMENT '创建者名称',
  `editor_id` int(11) NOT NULL DEFAULT 0 COMMENT '修改者id',
  `editor_name` varchar(30) NOT NULL DEFAULT '' COMMENT '修改者名称',
  `delete_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标注：0-否，1-是',
  `create_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) ON UPDATE current_timestamp(3) COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

INSERT INTO `sys_role` (`role_code`, `role_name`, `level`) VALUES ('superAdmin', '超级管理员', '1');

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `role_id` int(11) NOT NULL COMMENT '角色Id',
  `menu_id` int(11) NOT NULL COMMENT '菜单Id',
  `creator_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建者Id',
  `creator_name` varchar(30) NOT NULL DEFAULT '' COMMENT '创建者名称',
  `editor_id` int(11) NOT NULL DEFAULT 0 COMMENT '修改者id',
  `editor_name` varchar(30) NOT NULL DEFAULT '' COMMENT '修改者名称',
  `delete_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标注：0-否，1-是',
  `create_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) ON UPDATE current_timestamp(3) COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单表';

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) SELECT r.id, m.id FROM `sys_menu` m, `sys_role` r ORDER BY r.id, m.id;

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(30) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `dept_id` int(11) NOT NULL COMMENT '部门id',
  `role_level` tinyint(1) NOT NULL DEFAULT 3 COMMENT '普通角色级别',
  `email` varchar(128) NOT NULL DEFAULT '' COMMENT '邮箱',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 0锁定 1有效',
  `description` varchar(100) NOT NULL DEFAULT '' COMMENT '描述',
  `avatar` varchar(100) NOT NULL DEFAULT '' COMMENT '用户头像',
  `last_login_time` datetime NOT NULL DEFAULT current_timestamp() COMMENT '最近访问时间',
  `creator_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建者Id',
  `creator_name` varchar(30) NOT NULL DEFAULT '' COMMENT '创建者名称',
  `editor_id` int(11) NOT NULL DEFAULT 0 COMMENT '修改者id',
  `editor_name` varchar(30) NOT NULL DEFAULT '' COMMENT '修改者名称',
  `delete_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标注：0-否，1-是',
  `create_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) ON UPDATE current_timestamp(3) COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_email` (`email`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

INSERT INTO `sys_user` (`username`, `password`, `dept_id`, `role_level`) VALUES ('superAdmin', 'a0810d9f10cb00503a214b1d627d43a7', '0', '0');

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `creator_id` int(11) NOT NULL DEFAULT 0 COMMENT '创建者Id',
  `creator_name` varchar(30) NOT NULL DEFAULT '' COMMENT '创建者名称',
  `editor_id` int(11) NOT NULL DEFAULT 0 COMMENT '修改者id',
  `editor_name` varchar(30) NOT NULL DEFAULT '' COMMENT '修改者名称',
  `delete_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标注：0-否，1-是',
  `create_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT current_timestamp(3) ON UPDATE current_timestamp(3) COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

INSERT INTO `sys_user_role` (`user_id`, `role_id`) SELECT u.id, r.id FROM `sys_user` u, `sys_role` r ORDER BY u.id, r.id;
