package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RoleListRes {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("角色编码")
    private String roleCode;
    @ApiModelProperty("角色名称")
    private String roleName;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("创建者Id")
    private Long creatorId;
    @ApiModelProperty("创建者名称")
    private String creatorName;
    @ApiModelProperty("修改者id")
    private Long editorId;
    @ApiModelProperty("修改者名称")
    private String editorName;
    @ApiModelProperty("创建时间")
    private Long createTime;
    @ApiModelProperty("更新时间")
    private Long updateTime;
    @ApiModelProperty("是否可以编辑：0-不可编辑，1-可编辑")
    private Integer canEdit;

}
