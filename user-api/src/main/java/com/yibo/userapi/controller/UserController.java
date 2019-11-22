package com.yibo.userapi.controller;

import com.yibo.userapi.domain.UserInfo;
import com.yibo.userapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author: huangyibo
 * @Date: 2019/11/9 11:51
 * @Description:
 */

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserInfo create(@RequestBody @Validated UserInfo userInfo){

        return userService.create(userInfo);
    }

    /**
     * 用户登录情况下，防止session Fixation(固定)攻击
     * @param userInfo
     * @param request
     * @return
     */
    @PostMapping("/login")
    public UserInfo login(@RequestBody @Validated UserInfo userInfo,HttpServletRequest request){
        UserInfo userResult = userService.login(userInfo);
        if(userResult.getId() != null){
            //如果找不到session，不会创建新session
            HttpSession session = request.getSession(false);
            if(session != null){
                //登录情况下，如果session不为空，说明session之前有人用过，这里将session失效掉
                session.invalidate();
            }
            //然后创建session进行用户数据存储
            request.getSession(true).setAttribute("userInfo",userResult);
        }
        return userResult;
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request){

        request.getSession().invalidate();
    }

    @PutMapping("/{id}")
    public UserInfo update(@PathVariable("id") Integer id, @RequestBody UserInfo userInfo){

        return userService.update(userInfo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id){

        userService.delete(id);
    }

    @GetMapping("/{id}")
    public UserInfo get(@PathVariable("id") Integer id,HttpServletRequest request){
        UserInfo userInfo = (UserInfo)request.getSession().getAttribute("userInfo");
        if(userInfo == null || userInfo.getId() != id){
            throw new RuntimeException("身份认证信息异常，获取用户信息失败！");
        }
        return userService.get(id);
    }

    @GetMapping("/")
    public List<UserInfo> findAll(){

        return userService.findAll();
    }
}
