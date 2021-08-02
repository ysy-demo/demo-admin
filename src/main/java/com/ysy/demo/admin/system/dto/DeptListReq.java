package com.ysy.demo.admin.system.dto;

import com.ysy.demo.admin.dto.BasePageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DeptListReq extends BasePageReq {

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("名称")
    private String deptName;

}
