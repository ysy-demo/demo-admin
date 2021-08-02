package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class DictEditReq extends DictAddReq {

    @ApiModelProperty("id")
    @NotNull
    private Long id;

}
