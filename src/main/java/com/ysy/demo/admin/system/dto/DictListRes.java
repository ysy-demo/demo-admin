package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DictListRes {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("排序")
    private Double sort;
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

}
