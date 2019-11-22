package com.yibo.frontend.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.yibo.frontend.domain.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

/**
 * @author: huangyibo
 * @Date: 2019/11/12 21:50
 * @Description: 从cookie中获取token，适用于基于cookie的sso方案
 */

@Component
public class CookieTokenFilter extends ZuulFilter {

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() throws ZuulException {
    RequestContext requestContext = RequestContext.getCurrentContext();
    //得到request
    HttpServletRequest request = requestContext.getRequest();
    HttpServletResponse response = requestContext.getResponse();


    String accessToken = getCookie("yibo_access_token");
    if(!StringUtils.isEmpty(accessToken)){
      requestContext.addZuulRequestHeader("Authorization","bearer "+accessToken);
    }else{
      //accessToken过期，获取refreshToken
      String refreshToken = getCookie("yibo_refresh_token");
      if(!StringUtils.isEmpty(refreshToken)){
        //如果access_Token过期，使用refresh_token刷新access_Token
        String oauthServiceUrl = "http://gateway.yibo.com:9070/token/oauth/token";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth("serverFrontend","123456");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","refresh_token");
        params.add("refresh_token",refreshToken);

        try {
          //组装请求实体
          HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,httpHeaders);
          ResponseEntity<TokenInfo> newToken = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
          //获取到accessToken之后，将accessToken放入请求头中
          requestContext.addZuulRequestHeader("Authorization","bearer "+newToken.getBody().getAccess_token());
          //将token放在浏览器的cookie中
          Cookie accessTokenCookie = new Cookie("yibo_access_token",newToken.getBody().getAccess_token());
          accessTokenCookie.setMaxAge(newToken.getBody().getExpires_in().intValue());
          accessTokenCookie.setDomain("yibo.com");
          accessTokenCookie.setPath("/");
          accessTokenCookie.setHttpOnly(true);
          response.addCookie(accessTokenCookie);

          Cookie refreshTokenCookie = new Cookie("yibo_refresh_token",newToken.getBody().getRefresh_token());
          refreshTokenCookie.setMaxAge(2592000);
          refreshTokenCookie.setDomain("yibo.com");
          refreshTokenCookie.setPath("/");
          refreshTokenCookie.setHttpOnly(true);
          response.addCookie(refreshTokenCookie);

        } catch (Exception e) {
          //如果访问令牌过期，并且刷新令牌也失败了
          requestContext.setSendZuulResponse(false);//不继续向下执行
          requestContext.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());//设置状态为500
          requestContext.setResponseBody("{\"message\":\"refresh fail\"}");
          requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
      }else {
        //如果访问令牌过期，并且刷新令牌也失败了
        requestContext.setSendZuulResponse(false);//不继续向下执行
        requestContext.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());//设置状态为500
        requestContext.setResponseBody("{\"message\":\"refresh fail\"}");
        requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      }
    }
    return null;
  }

  private String getCookie(String cookieName) {
    RequestContext requestContext = RequestContext.getCurrentContext();
    //得到request
    HttpServletRequest request = requestContext.getRequest();
    Cookie[] cookies = request.getCookies();
    return Stream.of(cookies)
      .filter(cookie -> cookieName.equals(cookie.getName()))
      .map(cookie -> cookie.getValue())
      .findFirst()
      .orElse("");
  }
}
