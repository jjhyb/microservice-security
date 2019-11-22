package com.yibo.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author: huangyibo
 * @Date: 2019/11/11 18:28
 * @Description:
 */

@SpringBootApplication
@EnableZuulProxy
public class FrontendServerApplication {

  public static void main(String[] args) {

    SpringApplication.run(FrontendServerApplication.class,args);
  }

  @Bean
  public RestTemplate restTemplate(){

    return new RestTemplate();
  }
}
