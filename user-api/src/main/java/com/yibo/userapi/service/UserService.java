package com.yibo.userapi.service;

import com.lambdaworks.crypto.SCryptUtil;
import com.yibo.userapi.domain.UserInfo;
import com.yibo.userapi.domain.entity.MeUser;
import com.yibo.userapi.mapper.MeUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: huangyibo
 * @Date: 2019/11/9 13:07
 * @Description:
 */

@Service
public class UserService {

    @Autowired
    private MeUserMapper meUserMapper;


    public UserInfo create(UserInfo userInfo){
        if(StringUtils.isEmpty(userInfo.getNickname())){
            userInfo.setNickname(userInfo.getUsername());
        }
        MeUser user = new MeUser();
        BeanUtils.copyProperties(userInfo,user);
        user.setId(null);
        user.setPermissions("r");
        user.setPassword(SCryptUtil.scrypt(user.getPassword(),32768,8,1));
        meUserMapper.insertSelective(user);
        userInfo.setId(user.getId());
        return userInfo;
    }


    public UserInfo update(UserInfo userInfo){

        return userInfo;
    }


    public void delete(Integer id){


    }


    public UserInfo get(Integer id){
        UserInfo userInfo = new UserInfo();
        MeUser meUser = meUserMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(meUser,userInfo);
        return userInfo;
    }


    public List<UserInfo> findAll(){

        return new ArrayList<>();
    }

    public UserInfo login(UserInfo userInfo) {
        UserInfo userResult = new UserInfo();
        MeUser userQuery = new MeUser();
        userQuery.setUsername(userInfo.getUsername());
        MeUser meUser = meUserMapper.selectOne(userQuery);
        if(meUser != null && SCryptUtil.check(userInfo.getPassword(),meUser.getPassword())){
            BeanUtils.copyProperties(meUser,userResult);
        }
        return userResult;
    }
}
