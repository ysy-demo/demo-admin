package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel
public class UserAddReq {

    @ApiModelProperty("用户名")
    @NotBlank
    @Size(min = 2, max = 30)
    private String username;
    @ApiModelProperty("部门Id")
    @NotNull
    private Long deptId;
    @ApiModelProperty("邮箱")
    @NotBlank
    @Email
    private String email;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("角色")
    @NotEmpty
    private List<Long> roleIds;

}
