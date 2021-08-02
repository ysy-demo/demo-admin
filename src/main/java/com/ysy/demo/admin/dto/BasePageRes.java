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
public class BasePageRes<T> {

    public static final BasePageRes EMPTY = BasePageRes.builder().records(Collections.emptyList()).build();

    @ApiModelProperty("总条数")
    private long total;

    @ApiModelProperty("记录")
    private List<T> records;

}
