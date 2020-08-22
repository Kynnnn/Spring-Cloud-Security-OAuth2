package com.kynnnn.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Zhudiwei
 * @date 2020/08/22
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableFeignClients(basePackages = {"com.kynnnn.security"})
public class AuthoriztionApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthoriztionApplication.class,args);
    }
}
