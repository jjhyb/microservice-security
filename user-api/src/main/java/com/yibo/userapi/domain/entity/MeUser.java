package com.yibo.userapi.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "me_user")
public class MeUser {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户权限
     */
    private String permissions;

    /**
     * 性别:1男,0女
     */
    private String sex;

    /**
     * 手机号
     */
    private Integer phone;

    /**
     * 头像图片地址
     */
    @Column(name = "head_portrait")
    private String headPortrait;

    /**
     * 帐户是否过期:1未过期，0已过期
     */
    @Column(name = "account_non_expired")
    private String accountNonExpired;

    /**
     * 帐户是否锁定:1未锁定，0已锁定
     */
    @Column(name = "account_non_locked")
    private String accountNonLocked;

    /**
     * 用户的凭据(密码)是否过期:1未过期，0已过期
     */
    @Column(name = "credentials_non_expired")
    private String credentialsNonExpired;

    /**
     * 用户是否被禁用:1未禁用，0已禁用
     */
    private String enabled;

    /**
     * 账户创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 账户修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 判断用户是否有权限
     * @param method
     * @return
     */
    public boolean hasPermission(String method) {
        boolean result = false;
        if("GET".equalsIgnoreCase(method)){
            result = permissions.contains("r");
        }else {
            result = permissions.contains("w");
        }
        return result;
    }
}