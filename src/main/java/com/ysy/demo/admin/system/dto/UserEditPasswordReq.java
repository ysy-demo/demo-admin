package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ApiModel
public class UserEditPasswordReq {

    @ApiModelProperty("旧密码")
    @NotBlank
    @Size(min = 6)
    private String oldPassword;
    @ApiModelProperty("新密码")
    @NotBlank
    @Size(min = 6)
    private String newPassword;

}
