package com.yibo.userapi.filter;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: huangyibo
 * @Date: 2019/11/9 12:33
 * @Description: OncePerRequestFilter 保证请求只会在过滤器执行一次
 */

@Component
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter {

    /**
     * 创建流控的控制器
     */
    private RateLimiter rateLimiter = RateLimiter.create(1);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!rateLimiter.tryAcquire()){
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            PrintWriter writer = response.getWriter();
            writer.write("too many requests");
            writer.flush();
            return;
        }
        filterChain.doFilter(request,response);
    }
}
