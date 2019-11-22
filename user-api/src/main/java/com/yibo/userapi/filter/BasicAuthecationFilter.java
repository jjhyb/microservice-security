package com.yibo.userapi.filter;

import com.lambdaworks.crypto.SCryptUtil;
import com.yibo.userapi.domain.UserInfo;
import com.yibo.userapi.domain.entity.MeUser;
import com.yibo.userapi.mapper.MeUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author: huangyibo
 * @Date: 2019/11/9 14:30
 * @Description:
 */

@Component
@Order(2)
public class BasicAuthecationFilter extends OncePerRequestFilter {

    @Autowired
    private MeUserMapper meUserMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(authHeader)){
            String token64 = authHeader.substring("Basic ".length());
            String token = new String(Base64Utils.decodeFromString(token64));
            String[] items = StringUtils.split(token,":");
            String username = items[0];
            String password = items[1];
            MeUser userQuery = new MeUser();
            userQuery.setUsername(username);
            MeUser meUser = meUserMapper.selectOne(userQuery);
            if(meUser != null && SCryptUtil.check(password,meUser.getPassword())){
                UserInfo userInfo = new UserInfo();
                BeanUtils.copyProperties(meUser,userInfo);
                request.getSession().setAttribute("userInfo",userInfo);
                request.getSession().setAttribute("temp","yes");
            }
        }

        try {
            filterChain.doFilter(request,response);
        } finally {
            HttpSession session = request.getSession();
            if("yes".equals(session.getAttribute("temp"))){
                //如果是通过Authorization的Header请求通过的认证，在请求结束后销毁掉session
                session.invalidate();
            }
        }
    }

    public static void main(String[] args) {
        String str = "Basic abc:abc";
        String substring = str.substring("Basic ".length());
        System.out.println("["+substring+"]");
        String[] split = StringUtils.split(substring, ":");
        for (String s : split) {
            System.out.println(s);
        }
        System.out.println(split);
    }
}
