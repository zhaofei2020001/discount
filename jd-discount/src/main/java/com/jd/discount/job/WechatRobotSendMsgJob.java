package com.jd.discount.job;

import com.disount.common.constant.AllEnums;
import com.jd.discount.util.JDUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author zf
 * since 2019/12/20
 */
@EnableScheduling
public class WechatRobotSendMsgJob {

  //  @Scheduled(cron = "0/1 * * * * ?")
  public  static String sendMsg(RedisTemplate<String, Object> redisTemplate,AllEnums.eliteEnum eliteEnum) {
    String template = null;
    try {
      template = JDUtil.getJFGoodsRespByType(eliteEnum, redisTemplate);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return template;
  }

}
