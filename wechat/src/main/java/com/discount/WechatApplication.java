package com.discount;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author zf
 * since 2019/12/18
 */
@SpringCloudApplication
public class WechatApplication {
  public static void main(String[] args) {
    SpringApplication.run(WechatApplication.class, args);
    System.out.println("----------WechatApplication started successfully----------");
  }
}
