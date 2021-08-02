package com.ysy.demo.admin.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ResultEntity<T> implements Serializable {

    public static final String SUCCESS_CODE = "00000";
    public static final String FAIL_SYSTEM_ERROR_CODE = "00003";
    public static final String FAIL_INVALID_REQUEST_CODE = "00004";
    public static final ResultEntity SUCCESS = new ResultEntity(SUCCESS_CODE, "SUCCESSFUL");
    public static final ResultEntity FAILED_SYSTEM_ERROR = new ResultEntity(FAIL_SYSTEM_ERROR_CODE, "SYSTEM ERROR");
    public static final ResultEntity FAILED_INVALID_REQUEST = new ResultEntity(FAIL_INVALID_REQUEST_CODE, "INVALID REQUEST");

    @ApiModelProperty("编码：00000-成功，00004-参数异常，00003-系统异常")
    private String code;
    @ApiModelProperty("信息")
    private String message;
    @ApiModelProperty("返回数据")
    private T data;

    public ResultEntity(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultEntity(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResultEntity(Object code, String message) {
        this.code = code.toString();
        this.message = message;
    }

    public static <T> ResultEntity success(T data) {
        return new ResultEntity(SUCCESS_CODE, data);
    }

    public static <T> ResultEntity invalid(String message) {
        return new ResultEntity(FAIL_INVALID_REQUEST_CODE, message);
    }
}
