package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
@ApiModel
public class LoginRes {

    @ApiModelProperty("token")
    private String token;
    @ApiModelProperty("token 过期时间")
    private Long expireTime;
    @ApiModelProperty("角色名称")
    private Set<String> roles;
    @ApiModelProperty("权限")
    private Set<String> permissions;
    @ApiModelProperty("用户信息")
    private UserRes user;
}
