package com.itheima;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@MapperScan("com.itheima.mapper")
@ServletComponentScan
@SpringBootApplication
public class TliasWebManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(TliasWebManagementApplication.class, args);
        System.out.println("master分支修改了文件");
        System.out.println("dev-chap02分支修改了文件");
    }
}
