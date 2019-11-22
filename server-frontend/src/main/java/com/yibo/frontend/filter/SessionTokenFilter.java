package com.yibo.frontend.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.yibo.frontend.domain.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: huangyibo
 * @Date: 2019/11/12 0:09
 * @Description: 从session中获取token,这个是用于将token放在前端服务器中的session中的方案
 */

@Component
@Slf4j
public class SessionTokenFilter extends ZuulFilter {

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 0;
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
    TokenInfo token = (TokenInfo) request.getSession().getAttribute("token");
    if(token != null){
      String accessToken = token.getAccess_token();
      if(token.isExpired()){
        //如果access_Token过期，使用refresh_token刷新access_Token
        String oauthServiceUrl = "http://gateway.yibo.com:9070/token/oauth/token";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth("serverFrontend","123456");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","refresh_token");
        params.add("refresh_token",token.getRefresh_token());

        try {
          //组装请求实体
          HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params,httpHeaders);
          ResponseEntity<TokenInfo> newToken = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
          request.getSession().setAttribute("token",newToken.getBody().init());
          accessToken = newToken.getBody().getAccess_token();
        } catch (Exception e) {
          //如果访问令牌过期，并且刷新令牌也失败了
          requestContext.setSendZuulResponse(false);//不继续向下执行
          requestContext.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());//设置状态为500
          requestContext.setResponseBody("{\"message\":\"refresh fail\"}");
          requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
      }
      requestContext.addZuulRequestHeader("Authorization","bearer "+accessToken);
    }
    return null;
  }
}
