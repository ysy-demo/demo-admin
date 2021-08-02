package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UserListRes {

    @ApiModelProperty("用户Id")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("部门Id")
    private Long deptId;
    @ApiModelProperty("部门名称")
    private String deptName;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("状态：0-锁定，1-有效")
    private Integer status;
    @ApiModelProperty("最后登录时间")
    private Long lastLoginTime;
    @ApiModelProperty("创建者Id")
    private Long creatorId;
    @ApiModelProperty("创建者名称")
    private String creatorName;
    @ApiModelProperty("修改者id")
    private Long editorId;
    @ApiModelProperty("修改者名称")
    private String editorName;
    @ApiModelProperty("创建时间")
    private Long createTime;
    @ApiModelProperty("更新时间")
    private Long updateTime;
    @ApiModelProperty("是否可以编辑：0-不可编辑，1-可编辑")
    private Integer canEdit;

}
