package com.yibo.priceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author: huangyibo
 * @Date: 2019/11/10 13:28
 * @Description:
 */
//@MapperScan("com.yibo.priceapi.mapper")//扫描mybatis的指定包下的接口
@SpringBootApplication
@EnableResourceServer
public class PriceApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(PriceApiApplication.class, args);
    }

}
