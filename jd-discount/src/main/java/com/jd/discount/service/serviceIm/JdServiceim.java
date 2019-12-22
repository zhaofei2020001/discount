package com.jd.discount.service.serviceIm;

import com.disount.common.constant.AllEnums;
import com.disount.common.constant.Constants;
import com.jd.discount.service.JdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author zf
 * since 2019/12/21
 */
@Service
public class JdServiceim implements JdService {
  @Autowired
  RedisTemplate<String, Object> redisTemplate;


  @Override
  public boolean setMsgToRedis(String content, String imageName) {

    Long aLong = redisTemplate.opsForList().leftPush(AllEnums.wechatGroupEnum.XWW.getDesc(), content + Constants.SPLIT_FLAG + imageName);
    if (aLong != 0L) {
      return true;
    }
    return false;
  }
}
