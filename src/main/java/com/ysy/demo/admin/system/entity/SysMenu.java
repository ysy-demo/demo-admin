package com.ysy.demo.admin.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysMenu implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long parentId;
    private String menuName;
    private String perms;
    private Integer type;
    private Double sort;
    private Long creatorId;
    private String creatorName;
    private Long editorId;
    private String editorName;
    private Integer deleteFlag;
    private Date createTime;
    private Date updateTime;

}