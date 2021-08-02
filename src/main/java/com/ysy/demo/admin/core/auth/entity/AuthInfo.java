package com.ysy.demo.admin.core.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthInfo implements Serializable {

    private AuthUser user;
    private Set<String> roles;
    private Set<String> permissions;

}
