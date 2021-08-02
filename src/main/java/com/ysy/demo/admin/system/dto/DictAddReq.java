package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class DictAddReq {

    @ApiModelProperty("类型")
    @NotBlank
    private String type;
    @ApiModelProperty("编码")
    @NotBlank
    private String code;
    @ApiModelProperty("名称")
    @NotBlank
    private String name;
    @ApiModelProperty("排序")
    private Double sort;
}
