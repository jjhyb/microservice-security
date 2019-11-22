package com.yibo.frontend.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: huangyibo
 * @Date: 2019/11/11 19:21
 * @Description:
 */

@Data
public class TokenInfo {

  /**
   * 访问令牌
   */
  private String access_token;

  /**
   * 刷新令牌
   */
  private String refresh_token;

  /**
   * Access Token的类型
   */
  private String token_type;

  /**
   * 过期时间
   */
  private Long expires_in;

  /**
   * 授权范围（“scope”，与获取Authorization Code时指定的一致）以及表示认证身份的安全令牌（“authentication_token”）
   */
  private String scope;

  /**
   * 准确的过期时间点
   */
  private LocalDateTime expireTime;

  /**
   * 计算准确的过期时间点
   * 获取access_token的时候，当前时间加上过期时长计算出准确的过期时间点
   */
  public TokenInfo init(){
    //expires_in - 3 网络延迟等因素，这里设置access_token提前3秒到期
    expireTime = LocalDateTime.now().plusSeconds(expires_in - 3);
    return this;
  }

  /**
   * 判断access_token过期时间点是不是在当前时间之前
   * @return
   */
  public boolean isExpired() {

    return expireTime.isBefore(LocalDateTime.now());
  }
}
