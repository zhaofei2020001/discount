package com.discount.runner;

import com.discount.robot.HelloBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author zf
 * since 2019/12/18
 */
@Component
@Order(1)
@Slf4j
public class WechatRunner implements ApplicationRunner {

@Autowired
  HelloBot helloBot;
  @Override
  public void run(ApplicationArguments args) throws Exception {
    helloBot.start();
    log.info("机器人启动了----------------->");
  }
}
