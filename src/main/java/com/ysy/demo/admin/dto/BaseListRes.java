package com.ysy.demo.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class BaseListRes<T> {

    public static final List EMPTY = Collections.emptyList();

    @ApiModelProperty("记录")
    private List<T> rows;
}
