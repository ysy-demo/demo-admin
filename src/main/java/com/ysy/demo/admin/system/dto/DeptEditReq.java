package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class DeptEditReq extends DeptAddReq {

    @ApiModelProperty("id")
    @NotNull
    private Long id;

}
