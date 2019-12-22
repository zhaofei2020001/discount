package com.discount.robot;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.constant.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * @author zf
 * since 2019/12/18
 */
@Component
@Slf4j
public class HelloBot extends WeChatBot {
  @Autowired
  RedisTemplate<String, Object> redisTemplate;

  public HelloBot() {
    super(Config.me().autoLogin(true).showTerminal(true));
  }

}