package com.yibo.orderapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.alibaba.sentinel.annotation.SentinelRestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

/**
 * @author: huangyibo
 * @Date: 2019/11/10 13:28
 * @Description:
 */
//@MapperScan("com.yibo.orderapi.mapper")//扫描mybatis的指定包下的接口
@Configuration
@SpringBootApplication
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启方法权限控制，在方法执行之后，通过注解插入和方法相关的安全表达式
@EnableFeignClients
public class OrderApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(OrderApiApplication.class, args);
    }

    //此参数SpringBoot已经声明好了只需要注入即可用
    /*@Autowired
    private OAuth2ProtectedResourceDetails resource;

    //此参数SpringBoot已经声明好了只需要注入即可用
    @Autowired
    private OAuth2ClientContext context;*/

    //OAuth2RestTemplate在发送请求的时候，在Http请求头里面会自动放入收到的token
    /*@Bean
    @LoadBalanced
    public OAuth2RestTemplate oAuth2RestTemplate(){

        return new OAuth2RestTemplate(resource,context);
    }*/

    @Bean
    @LoadBalanced
    @SentinelRestTemplate
    public RestTemplate RestTemplate(){

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}
