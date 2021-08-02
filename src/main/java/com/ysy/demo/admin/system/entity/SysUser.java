package com.ysy.demo.admin.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysUser implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private Long deptId;
    private Integer roleLevel;
    private String email;
    private Integer status;
    private String description;
    private String avatar;
    private Date lastLoginTime;
    private Long creatorId;
    private String creatorName;
    private Long editorId;
    private String editorName;
    private Integer deleteFlag;
    private Date createTime;
    private Date updateTime;

}
