package com.yibo.server.domain.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
public class MeUserDO {
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
     * 账户是否过期(1未过期，0已过期),过期无法验证
     * @return
     */
    public boolean isAccountNonExpired(){

        return "1".equals(this.accountNonExpired);
    }

    /**
     * 指定用户是否被锁定或者解锁(1未锁定，0已锁定),锁定的用户无法进行身份验证
     *
     * @return
     */
    public boolean isAccountNonLocked(){

        return "1".equals(this.accountNonExpired);
    }

    /**
     * 指示是否已过期的用户的凭据(密码，1未过期，0已过期),过期的凭据防止认证
     * @return
     */
    public boolean isCredentialsNonExpired(){

        return "1".equals(this.accountNonExpired);
    }

    /**
     * 是否被禁用(1未禁用，0已禁用),禁用的用户不能身份验证
     * @return
     */
    public boolean isEnabled(){

        return "1".equals(this.accountNonExpired);
    }
}