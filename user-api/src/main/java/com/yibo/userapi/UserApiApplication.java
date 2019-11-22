package com.yibo.userapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.yibo.userapi.mapper")//扫描mybatis的指定包下的接口
@SpringBootApplication
public class UserApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(UserApiApplication.class, args);
    }

}
