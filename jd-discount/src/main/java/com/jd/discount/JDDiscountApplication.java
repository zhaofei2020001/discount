package com.jd.discount;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zf
 * since 2019/12/16
 */
@SpringCloudApplication
@EnableDiscoveryClient
public class JDDiscountApplication {
  public static void main(String[] args) {
    SpringApplication.run(JDDiscountApplication.class, args);
    System.out.println("----------JDDiscountApplication started successfully----------");
  }
}
