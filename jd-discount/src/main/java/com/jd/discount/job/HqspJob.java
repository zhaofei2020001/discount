package com.jd.discount.job;

import com.alibaba.fastjson.JSONObject;
import com.disount.common.constant.AllEnums;
import com.jd.discount.util.JXGoodsQueryUtil;
import jd.union.open.goods.jingfen.query.response.JFGoodsResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author zf
 * since 2019/12/17
 */
@Slf4j
@Configuration
@EnableScheduling
public class HqspJob {
  @Autowired
  RedisTemplate<String, Object> redisTemplate;

  /**
   * 好券商品
   */
  public void test() {

    for (AllEnums.eliteEnum value : AllEnums.eliteEnum.values()) {
      log.info("TYPE----------------------------->{}", value.getDesc());

      List<JFGoodsResp> jfGoodsRespList = JXGoodsQueryUtil.jxGoodsquery(value);

      Collections.sort(jfGoodsRespList, new Comparator<JFGoodsResp>() {
        @Override
        public int compare(JFGoodsResp t1, JFGoodsResp t2) {
          // 按照学生的年龄进行升序排列
          if (t1.getInOrderCount30DaysSku().intValue() > t2.getInOrderCount30DaysSku().intValue()) {
            return 1;
          }
          if (t1.getInOrderCount30DaysSku().intValue() == t2.getInOrderCount30DaysSku().intValue()) {
            return 0;
          }
          return -1;
        }
      });

      log.info("size---------->{}", jfGoodsRespList.size());

      for (int i = 0; i < jfGoodsRespList.size(); i++) {
        log.info("skuId---->{},skuName---->{},------->{}", jfGoodsRespList.get(i).getSkuId(), jfGoodsRespList.get(i).getSkuName(), i);
        redisTemplate.opsForList().leftPush(value.getDesc(), JSONObject.toJSONString(jfGoodsRespList.get(i)));
      }
    }
  }

}
