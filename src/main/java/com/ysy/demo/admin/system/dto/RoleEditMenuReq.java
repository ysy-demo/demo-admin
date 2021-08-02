package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel
public class RoleEditMenuReq {

    @ApiModelProperty("id")
    @NotNull
    private Long id;

    @ApiModelProperty("菜单Ids")
    @NotEmpty
    private List<Long> menuIds;

}
