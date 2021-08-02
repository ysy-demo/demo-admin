package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class UserRes {

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
    @ApiModelProperty("最后登录时间")
    private Long lastLoginTime;
    @ApiModelProperty("创建时间")
    private Long createTime;

}
