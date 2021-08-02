package com.ysy.demo.admin.system.dto;

import lombok.Data;

@Data
public class UserEditConfigReq {

    /**
     * 系统主题 dark暗色风格，light明亮风格
     */
    private String theme;

    /**
     * 系统布局 side侧边栏，head顶部栏
     */
    private String layout;

    /**
     * 页面风格 1多标签页 0单页
     */
    private Integer multiPage;

    /**
     * 页面滚动是否固定侧边栏 1固定 0不固定
     */
    private Integer fixedSidebar;

    /**
     * 页面滚动是否固定顶栏 1固定 0不固定
     */
    private Integer fixedHeader;

    /**
     * 主题颜色 RGB值
     */
    private String color;

}
