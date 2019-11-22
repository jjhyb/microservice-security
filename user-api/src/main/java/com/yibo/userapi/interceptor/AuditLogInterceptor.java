package com.yibo.userapi.interceptor;

import com.yibo.userapi.domain.UserInfo;
import com.yibo.userapi.domain.entity.AuditLog;
import com.yibo.userapi.mapper.AuditLogMapperExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author: huangyibo
 * @Date: 2019/11/9 17:16
 * @Description: HandlerInterceptorAdapter 拦截器适配器类
 */

@Component
public class AuditLogInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AuditLogMapperExt auditLogMapperExt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuditLog auditLog = new AuditLog();
        auditLog.setMethod(request.getMethod());
        auditLog.setPath(request.getRequestURI());
        //请求进来状态都设置为200
        auditLog.setStatus(0);
        UserInfo userInfo = (UserInfo)request.getSession().getAttribute("userInfo");
        if(userInfo != null){
            auditLog.setUsername(userInfo.getUsername());
        }else {
            //未登录用户使用匿名
            auditLog.setUsername("Anonymous");
        }
        auditLog.setCreatedTime(new Date());
        auditLog.setModifyTime(auditLog.getCreatedTime());
        auditLogMapperExt.insertAuditLog(auditLog);
        request.setAttribute("auditLogId",auditLog.getId());
        return true;
    }

    /**
     * 不管处理成功还是失败，都会调用此方法
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Integer auditLogId = (Integer)request.getAttribute("auditLogId");
        AuditLog auditLog = auditLogMapperExt.selectByPrimaryKey(auditLogId);
        auditLog.setStatus(response.getStatus());
        auditLog.setModifyTime(new Date());
        auditLogMapperExt.updateByPrimaryKey(auditLog);
    }
}
