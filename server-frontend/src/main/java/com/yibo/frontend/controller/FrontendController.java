package com.yibo.frontend.controller;

import com.yibo.frontend.domain.Credentials;
import com.yibo.frontend.domain.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: huangyibo
 * @Date: 2019/11/11 19:09
 * @Description: 这是模拟NodeJS的服务端，页面的请求到达这里后，添加appId和appSecret，将请求转发到后端
 */

@RestController
@Slf4j
public class FrontendController {

  @Autowired
  private RestTemplate restTemplate;

  /**
   * 这是在Oauth2的密码模式下模拟前端服务器端向认证服务器发起登录请求的
   * @param credentials
   * @param request
   */
  @PostMapping("/login")
  public void login(@RequestBody Credentials credentials, HttpServletRequest request){
    String oauthServiceUrl = "http://localhost:9070/token/oauth/token";
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.setBasicAuth("serverFrontend","123456");

    MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
    params.add("username",credentials.getUsername());
    params.add("password",credentials.getPassword());
    params.add("grant_type","password");
    params.add("scope","read write");

    //组装请求实体
    HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,httpHeaders);
    ResponseEntity<TokenInfo> responseEntity = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
    request.getSession().setAttribute("token",responseEntity.getBody().init());
  }

  /**
   * 这是在Oauth2的授权码模式下，认证中心给前端服务器发授权码的的地址，
   * 前端服务器获取到授权码之后再拿授权码、appId、appsecret去认证中心获取token
   * @param code
   * @param state
   * @param request
   * @param response
   * @throws IOException
   */
  @GetMapping("/oauth/callback")
  public void callback(@RequestParam String code, String state, HttpServletRequest request, HttpServletResponse response) throws IOException {
    log.info("state={}",state);
    String oauthServiceUrl = "http://gateway.yibo.com:9070/token/oauth/token";
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.setBasicAuth("serverFrontend","123456");

    MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
    params.add("code",code);
    params.add("grant_type","authorization_code");
    params.add("redirect_uri","http://frontend.yibo.com:8080/oauth/callback");

    //组装请求实体
    HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,httpHeaders);
    ResponseEntity<TokenInfo> token = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);

    //将token放在前端服务器的session中
//    request.getSession().setAttribute("token",token.getBody().init());

    //将token放在浏览器的cookie中
    Cookie accessTokenCookie = new Cookie("yibo_access_token",token.getBody().getAccess_token());
    accessTokenCookie.setMaxAge(token.getBody().getExpires_in().intValue());
    accessTokenCookie.setDomain("yibo.com");
    accessTokenCookie.setPath("/");
    accessTokenCookie.setHttpOnly(true);
    response.addCookie(accessTokenCookie);

    Cookie refreshTokenCookie = new Cookie("yibo_refresh_token",token.getBody().getRefresh_token());
    refreshTokenCookie.setMaxAge(2592000);
    refreshTokenCookie.setDomain("yibo.com");
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setHttpOnly(true);//这里设置为true，浏览器会获取不到cookie
    response.addCookie(refreshTokenCookie);

    //前端服务器获取到token之后，跳转页面，在浏览器上展示授权通过的效果
    response.sendRedirect("/");//这里应该是跳转到浏览器请求带过来的state的值上面，这里就直接跳转到根目录
  }

  /**
   * 前端服务器将token放在session中的时候，首先会调用此方法判断用户是否登录
   * @param request
   * @return
   */
  @GetMapping("/me")
  public TokenInfo getMe(HttpServletRequest request){
    TokenInfo token = (TokenInfo) request.getSession().getAttribute("token");
    return token;
  }

  @PostMapping("/logout")
  public void login(HttpServletRequest request,HttpServletResponse response){
    //直接将session失效掉
    request.getSession().invalidate();
    //用同名cookie替换掉之前的cookie，如果上面设置setHttpOnly(true)那么浏览器获取不到cookie,退出的话要在这里处理
    Cookie accessTokenCookie = new Cookie("yibo_access_token",null);
    accessTokenCookie.setMaxAge(0);
    accessTokenCookie.setDomain("yibo.com");
    accessTokenCookie.setPath("/");
    accessTokenCookie.setHttpOnly(true);
    response.addCookie(accessTokenCookie);

    Cookie refreshTokenCookie = new Cookie("yibo_refresh_token",null);
    refreshTokenCookie.setMaxAge(0);
    refreshTokenCookie.setDomain("yibo.com");
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setHttpOnly(true);
    response.addCookie(refreshTokenCookie);
  }
}
