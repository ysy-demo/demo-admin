package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ApiModel
@ToString(callSuper = true)
public class UserDetailRes extends UserListRes {

    @ApiModelProperty("角色Id")
    private List<Long> roleIds;

}
