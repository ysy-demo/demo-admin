package com.ysy.demo.admin.core.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser implements Serializable {
    private Long id;
    private String username;
    private String password;
    private Integer roleLevel;
}
