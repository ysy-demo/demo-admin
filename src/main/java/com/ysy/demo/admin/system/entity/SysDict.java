package com.ysy.demo.admin.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysDict implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String type;
    private String code;
    private String name;
    private Double sort;
    private Long creatorId;
    private String creatorName;
    private Long editorId;
    private String editorName;
    private Integer deleteFlag;
    private Date createTime;
    private Date updateTime;
}
