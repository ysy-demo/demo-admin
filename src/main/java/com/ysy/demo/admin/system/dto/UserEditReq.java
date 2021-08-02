package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UserEditReq extends UserAddReq {

    @ApiModelProperty("id")
    @NotNull
    private Long id;

    @ApiModelProperty("状态：0-锁定，1-有效")
    private Integer status;

}
