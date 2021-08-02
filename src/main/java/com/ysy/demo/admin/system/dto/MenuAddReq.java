package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel
public class MenuAddReq {

    @ApiModelProperty("父类Id：一级传 0")
    @NotNull
    private Long parentId;
    @ApiModelProperty("名称")
    @NotBlank
    @Size(min = 2, max = 30)
    private String menuName;
    @ApiModelProperty("权限")
    @NotBlank
    private String perms;
    @ApiModelProperty("类型：0-菜单，1-按钮")
    @NotNull
    private Integer type;
    @ApiModelProperty("排序")
    private Double sort;

}
