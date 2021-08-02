package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class RoleEditReq extends RoleAddReq {

    @ApiModelProperty("id")
    @NotNull
    private Long id;

}
