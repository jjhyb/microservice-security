package com.yibo.server.auth;

import com.yibo.server.domain.dto.MeUserDO;
import com.yibo.server.domain.entity.MeUser;
import com.yibo.server.mapper.MeUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: huangyibo
 * @Date: 2019/11/10 15:26
 * @Description:
 */

@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MeUserMapper meUserMapper;

    /**
     * 按微服务的拆分，这里应该远程调用用户中心，查询用户进行认证，这里为简单直接查询数据库
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MeUser userQuery = new MeUser();
        userQuery.setUsername(username);
        MeUser meUser = meUserMapper.selectOne(userQuery);
        if(meUser == null){
            log.info("用户名：["+username + "] 不存在");
            throw new BadCredentialsException("用户名：["+username + "] 不存在");
        }
        MeUserDO meUserDO = new MeUserDO();
        BeanUtils.copyProperties(meUser,meUserDO);

        /*GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);*/

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN");
        UserJwt userJwt = new UserJwt(username,meUserDO.getPassword(),
                meUserDO.isEnabled(),
                meUserDO.isAccountNonExpired(),
                meUserDO.isCredentialsNonExpired(),
                meUserDO.isAccountNonLocked()
                ,authorities);
        BeanUtils.copyProperties(meUserDO,userJwt);

        return userJwt;
    }
}
