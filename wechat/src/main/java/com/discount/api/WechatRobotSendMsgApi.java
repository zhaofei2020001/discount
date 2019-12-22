package com.discount.api;

import com.discount.robot.HelloBot;
import com.disount.common.constant.AllEnums;
import com.disount.common.constant.Constants;
import com.disount.common.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author zf
 * since 2019/12/21
 */
@RestController
@RequestMapping("/wechat/")
public class WechatRobotSendMsgApi {

  @Autowired
  HelloBot helloBot;
  @Autowired
  RedisTemplate<String, Object> redisTemplate;

  @GetMapping("/send")
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
      helloBot.sendMsg(helloBot.api().getAccountByName(AllEnums.wechatGroupEnum.XWW.getDesc()).getUserName(), messageContent);
      helloBot.sendImg(helloBot.api().getAccountByName(AllEnums.wechatGroupEnum.XWW.getDesc()).getUserName(), Utils.getAllImagePath(pictureName));
    }
  }
}
