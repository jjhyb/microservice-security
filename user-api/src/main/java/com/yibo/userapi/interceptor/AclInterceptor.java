package com.yibo.userapi.interceptor;

import com.yibo.userapi.domain.UserInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: huangyibo
 * @Date: 2019/11/9 22:02
 * @Description:
 */

@Component
public class AclInterceptor extends HandlerInterceptorAdapter {

    /**
     * 登录接口不需要认证
     */
    private String[] permitUrls = new String[]{"/users/login"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = true;
        if(!ArrayUtils.contains(permitUrls,request.getRequestURI())){
            UserInfo userInfo = (UserInfo)request.getSession().getAttribute("userInfo");
            if(userInfo == null){
                response.setContentType("text/plain");
                response.getWriter().write("need authentication");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                result = false;
            }else {
                String method = request.getMethod();
                if(!userInfo.hasPermission(method)){
                    response.setContentType("text/plain");
                    response.getWriter().write("forbidden");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    result = false;
                }
            }
        }
        return result;
    }
}
