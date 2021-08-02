package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel
public class RoleAddReq {

    @ApiModelProperty("角色编码")
    @NotBlank
    @Size(min = 2, max = 20)
    private String roleCode;
    @ApiModelProperty("角色名称")
    @NotBlank
    @Size(min = 2, max = 30)
    private String roleName;
    @ApiModelProperty("级别：1-超级，2-管理，3-普通")
    @NotNull
    private Integer level;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("菜单Ids")
    private List<Long> menuIds;

}
