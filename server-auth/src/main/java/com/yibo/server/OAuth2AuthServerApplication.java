package com.yibo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author: huangyibo
 * @Date: 2019/11/10 14:31
 * @Description:
 */

@MapperScan("com.yibo.server.mapper")//扫描Mapper接口
@SpringBootApplication
public class OAuth2AuthServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(OAuth2AuthServerApplication.class,args);
    }
}
