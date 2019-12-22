package com.jd.discount;

import com.alibaba.fastjson.JSONObject;
import com.disount.common.constant.AllEnums;
import com.disount.common.constant.Constants;
import com.google.common.collect.Lists;
import com.jd.discount.job.HqspJob;
import com.jd.discount.job.WechatRobotSendMsgJob;
import com.jd.discount.util.JDUtil;
import com.jd.discount.util.JXGoodsQueryUtil;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import jd.union.open.goods.jingfen.query.request.JFGoodsReq;
import jd.union.open.goods.jingfen.query.request.UnionOpenGoodsJingfenQueryRequest;
import jd.union.open.goods.jingfen.query.response.Coupon;
import jd.union.open.goods.jingfen.query.response.JFGoodsResp;
import jd.union.open.goods.jingfen.query.response.UnionOpenGoodsJingfenQueryResponse;
import jd.union.open.goods.promotiongoodsinfo.query.response.PromotionGoodsResp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zf
 * since 2019/12/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JDDiscountApplication.class})
public class JDTest {
  @Autowired
  HqspJob hqspJob;
  @Autowired
  RedisTemplate<String, Object> redisTemplate;


//  @Test
//  public void hqspJobtest() {
//    Set<String> keys = redisTemplate.keys("*");
//    redisTemplate.delete(keys);
//    System.out.println("*************************************************缓存已清空*************************************************");
//    //将京东商品放入缓存
//    hqspJob.test();
//
//
//  }

    @Test
  public void getSmsStr() {
    //从缓存中每种类型中取出一件商品并转为发消息的字符串
    for (AllEnums.eliteEnum value : AllEnums.eliteEnum.values()) {
      System.out.println("*************************************************" + value.getDesc() + "************************************************");
      System.out.println(WechatRobotSendMsgJob.sendMsg(redisTemplate, value));
//    System.out.println(WechatRobotSendMsgJob.sendMsg(redisTemplate, AllEnums.eliteEnum.JRBJ));
    }
  }

}
