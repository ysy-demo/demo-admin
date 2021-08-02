package com.ysy.demo.admin.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HandelResult {

    public static final HandelResult SUCCESS = HandelResult.builder().success(true).build();

    private boolean success;

    private String message;

}
