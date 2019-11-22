package com.yibo.userapi.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: huangyibo
 * @Date: 2019/11/9 11:50
 * @Description:
 */

@Data
public class UserInfo {

    private Integer id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    private String nickname;

    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 用户权限
     */
    private String permissions;

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
