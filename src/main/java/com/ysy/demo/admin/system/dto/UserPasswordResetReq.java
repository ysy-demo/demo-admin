package com.ysy.demo.admin.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel
public class UserPasswordResetReq {

    @ApiModelProperty("ids")
    @NotEmpty
    private List<Long> ids;

}
