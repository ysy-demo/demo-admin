package com.ysy.demo.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class BasePageReq {

    @ApiModelProperty("页数")
    private Integer page;

    @ApiModelProperty("条数")
    private Integer pageSize;

}
