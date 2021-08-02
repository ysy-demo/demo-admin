package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel
public class DeptAddReq {

    @ApiModelProperty("父类Id：一级传 0")
    @NotNull
    private Long parentId;

    @ApiModelProperty("名称")
    @NotBlank
    @Size(min = 2, max = 30)
    private String deptName;

    @ApiModelProperty("排序")
    private Double sort;

}
