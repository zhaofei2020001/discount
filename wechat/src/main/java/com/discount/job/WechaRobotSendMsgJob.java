package com.discount.job;

import com.discount.robot.HelloBot;
import com.disount.common.constant.AllEnums;
import com.disount.common.constant.Constants;
import com.disount.common.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;

/**
 * @author zf
 * since 2019/12/21
 */
@EnableScheduling
@Configuration
@Slf4j
public class WechaRobotSendMsgJob {

  @Autowired
  HelloBot helloBot;
  @Autowired
  RedisTemplate<String, Object> redisTemplate;
//  @Autowired
//  HqspJob hqspJob;



//  /**
//   * 每日凌晨清空所有数据
//   */
//  @Scheduled(cron = "59 59 23 * * ?")
//  public void cleanJdRredis() {
//    Set<String> keys = redisTemplate.keys("*");
//    redisTemplate.delete(keys);
//    hqspJob.test();
//    System.out.println(1);
//  }


  @Scheduled(cron = "0/1 20 * * * ?")
  public void sendMsg() {

    Object o = redisTemplate.opsForList().leftPop(AllEnums.wechatGroupEnum.XWW.getDesc());
    if (Objects.isNull(o)) {
      return;
    }
    String smsStr = (String) o;

    String[] split = smsStr.split(Constants.SPLIT_FLAG);
    //消息内容
    String messageContent = split[0];
    //消息图片路径
    String pictureName = split[1];
    if (io.github.biezhi.wechat.utils.StringUtils.isNotEmpty(messageContent)) {
      boolean b = helloBot.sendMsg(helloBot.api().getAccountByName("小窝窝").getUserName(), messageContent);
      log.info("机器人发送消息结果:------->{}", b);
      boolean b1 = helloBot.sendImg(helloBot.api().getAccountByName("小窝窝").getUserName(), Utils.getAllImagePath(pictureName));
      log.info("机器人发送图片结果:------->{}", b1);
    }
  }
}
