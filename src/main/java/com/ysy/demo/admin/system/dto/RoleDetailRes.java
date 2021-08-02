package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ApiModel
@ToString(callSuper = true)
public class RoleDetailRes extends RoleListRes {

    @ApiModelProperty("菜单Ids")
    private List<Long> menuIds;

}
