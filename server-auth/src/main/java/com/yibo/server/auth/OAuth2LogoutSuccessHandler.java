package com.yibo.server.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: huangyibo
 * @Date: 2019/11/12 16:31
 * @Description: 退出成功之后的处理器
 */

@Component
public class OAuth2LogoutSuccessHandler implements LogoutSuccessHandler {

    /**
     * 如果退出请求的参数包含redirect_uri，那么退出成功之后跳转到redirect_uri路径上
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String redirectUri = request.getParameter("redirect_uri");
        if(!StringUtils.isEmpty(redirectUri)){
            response.sendRedirect(redirectUri);
        }
    }
}
