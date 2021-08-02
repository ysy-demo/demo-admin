package com.ysy.demo.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseTreeRes<T> {

    public static final BaseTreeRes EMPTY = new BaseTreeRes();

    private T node;

    private List<BaseTreeRes<T>> child;

}
