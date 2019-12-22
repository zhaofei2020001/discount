package com.jd.discount.util;

import com.alibaba.fastjson.JSONObject;
import com.disount.common.constant.AllEnums;
import com.disount.common.constant.Constants;
import com.google.common.collect.Lists;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import jd.union.open.goods.jingfen.query.response.Coupon;
import jd.union.open.goods.jingfen.query.response.JFGoodsResp;
import jd.union.open.goods.promotiongoodsinfo.query.request.UnionOpenGoodsPromotiongoodsinfoQueryRequest;
import jd.union.open.goods.promotiongoodsinfo.query.response.PromotionGoodsResp;
import jd.union.open.goods.promotiongoodsinfo.query.response.UnionOpenGoodsPromotiongoodsinfoQueryResponse;
import jd.union.open.promotion.common.get.request.PromotionCodeReq;
import jd.union.open.promotion.common.get.request.UnionOpenPromotionCommonGetRequest;
import jd.union.open.promotion.common.get.response.UnionOpenPromotionCommonGetResponse;
import org.apache.ibatis.reflection.ArrayUtil;
import org.joda.time.DateTime;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author zf
 * since 2019/12/16
 */
public class JDUtil {
  /**
   * 转连
   *
   * @param linkUrl 原连接
   * @return 转连后的连接(通过转连后的连接下单 ， 可获取佣金)
   */
  public static String toLink(String linkUrl) throws JdException {
    JdClient client = new DefaultJdClient(Constants.JD_SERVER_URL, null, Constants.JD_APP_KEY, Constants.JD_APP_SECRET);
    UnionOpenPromotionCommonGetRequest request = new UnionOpenPromotionCommonGetRequest();
    PromotionCodeReq promotionCodeReq = new PromotionCodeReq();
    promotionCodeReq.setMaterialId(linkUrl);
    promotionCodeReq.setSiteId(Constants.social_media);

    request.setPromotionCodeReq(promotionCodeReq);
    UnionOpenPromotionCommonGetResponse response = client.execute(request);
    if (Objects.equals(response.getCode(), 200) && Objects.equals(response.getMessage(), "success")) {
      return response.getData().getClickURL();
    } else {
      return null;
    }
  }

  public static List<PromotionGoodsResp> queryGoods(Long skuIds) throws JdException {
    JdClient client = new DefaultJdClient(Constants.JD_SERVER_URL, null, Constants.JD_APP_KEY, Constants.JD_APP_SECRET);
    UnionOpenGoodsPromotiongoodsinfoQueryRequest request = new UnionOpenGoodsPromotiongoodsinfoQueryRequest();
    request.setSkuIds(skuIds.toString());
    UnionOpenGoodsPromotiongoodsinfoQueryResponse response = client.execute(request);


    if (Objects.equals(response.getCode(), 200) && Objects.equals(response.getMessage(), "接口成功")) {
      List<PromotionGoodsResp> promotionGoodsResps = CollectionUtils.arrayToList(response.getData());
      return promotionGoodsResps;
    } else {
      return Lists.newArrayList();
    }
  }

  /**
   * 根据商品类型从redis中取出一件商品并组装成可以发送到微信群字符串的模式
   *
   * @return
   */
  public static String getJFGoodsRespByType(AllEnums.eliteEnum eliteEnum, RedisTemplate<String, Object> redisTemplate) throws Exception {
    StringBuilder sbTemplate = new StringBuilder();

    //9.9专区
//    if (Objects.equals(AllEnums.eliteEnum.NINENINEZQ.getDesc(), eliteEnum.getDesc())) {
    String ninezq;
    try {
      ninezq = (String) redisTemplate.opsForList().leftPop(eliteEnum.getDesc());
    } catch (Exception e) {
      return null;
    }


    JFGoodsResp jfGoodsResps = JSONObject.parseObject(ninezq, JFGoodsResp.class);

    if(Objects.isNull(jfGoodsResps)){
      return "*******************************"+eliteEnum.getDesc()+"已从缓存中取空*******************************";
    }

    List<Coupon> list = CollectionUtils.arrayToList(jfGoodsResps.getCouponInfo().getCouponList());

    StringBuilder str = new StringBuilder();

    //如果是秒杀/拼购商品 返回秒杀拼购价,否则返回原价
    BigDecimal priceIfPinGouSeckillInfo = Utils.getPriceIfPinGouSeckillInfo(jfGoodsResps);


    //最优优惠完组合
    List<Coupon> maxCoupon = Utils.findMaxCoupon(list, new BigDecimal(jfGoodsResps.getPriceInfo().getPrice().toString()));

    //优惠券组合金额
    Double count = maxCoupon.stream().mapToDouble(Coupon::getDiscount).sum();


    //实际价格
    BigDecimal totalMoney = priceIfPinGouSeckillInfo.subtract(new BigDecimal(count.toString()));

    //有优惠券
    if (maxCoupon.size() > 0) {
      //有优惠券并且 将来会有秒杀信息
      if (Objects.nonNull(jfGoodsResps.getSeckillInfo()) && Objects.nonNull(jfGoodsResps.getSeckillInfo().getSeckillStartTime()) && (DateTime.now().isBefore(jfGoodsResps.getSeckillInfo().getSeckillStartTime()))) {

        if (maxCoupon.size() == 1) {
          str.append("领券:").append("\r\n").append(Utils.toHttpUrl(maxCoupon.get(0).getLink())).append("\r\n")
              .append("———").append("\r\n").append("预告：该产品将于【").append(new DateTime(jfGoodsResps.getSeckillInfo().getSeckillStartTime()).toString("yyyy-MM-dd HH:mm:ss")).append("】参与秒杀更优惠！！！");
        }
        if (maxCoupon.size() > 1) {
          for (int i = 1; i <= maxCoupon.size(); i++) {
            str.append("第").append(i).append("张券:").append("\r\n").append(Utils.toHttpUrl(maxCoupon.get(i - 1).getLink())).append("\r\n");
          }
          str.append("———").append("\r\n").append("预告：该产品将于【").append(new DateTime(jfGoodsResps.getSeckillInfo().getSeckillStartTime()).toString("yyyy-MM-dd HH:mm:ss")).append("】参与秒杀更优惠！！！");

        }

      }


      //有优惠券并且 将来会有拼团购信息
      if (Objects.nonNull(jfGoodsResps.getPinGouInfo()) && Objects.nonNull(jfGoodsResps.getPinGouInfo().getPingouStartTime()) && (DateTime.now().isBefore(jfGoodsResps.getPinGouInfo().getPingouStartTime()))) {

        if (maxCoupon.size() == 1) {
          str.append("领券:").append("\r\n").append(Utils.toHttpUrl(maxCoupon.get(0).getLink())).append("\r\n")
              .append("———").append("\r\n").append("预告：该产品将于【").append(new DateTime(jfGoodsResps.getPinGouInfo().getPingouStartTime()).toString("yyyy-MM-dd HH:mm:ss")).append("】参与拼团购更优惠！！！");
        }
        if (maxCoupon.size() > 1) {
          for (int i = 1; i <= maxCoupon.size(); i++) {
            str.append("第").append(i).append("张券:").append("\r\n").append(Utils.toHttpUrl(maxCoupon.get(i - 1).getLink())).append("\r\n");
          }
          str.append("———").append("\r\n").append("预告：该产品将于【").append(new DateTime(jfGoodsResps.getPinGouInfo().getPingouStartTime()).toString("yyyy-MM-dd HH:mm:ss")).append("】参与拼团购更优惠！！！");
        }
      }

      //有优惠券 未来即无秒杀又无拼团
      if (!(Objects.nonNull(jfGoodsResps.getSeckillInfo()) && Objects.nonNull(jfGoodsResps.getSeckillInfo().getSeckillStartTime()) && (DateTime.now().isBefore(jfGoodsResps.getSeckillInfo().getSeckillStartTime()))) && !(Objects.nonNull(jfGoodsResps.getPinGouInfo()) && Objects.nonNull(jfGoodsResps.getPinGouInfo().getPingouStartTime()) && (DateTime.now().isBefore(jfGoodsResps.getPinGouInfo().getPingouStartTime())))) {
        if (maxCoupon.size() == 1) {
          str.append("领券:").append("\r\n").append(Utils.toHttpUrl(maxCoupon.get(0).getLink())).append("\r\n");
        }
        if (maxCoupon.size() > 1) {
          for (int i = 1; i <= maxCoupon.size(); i++) {
            str.append("第").append(i).append("张券:").append("\r\n").append(Utils.toHttpUrl(maxCoupon.get(i - 1).getLink())).append("\r\n");
          }
        }
      }

      //无优惠
    } else {
      //无优惠券但 将来会有秒杀信息
      if (Objects.nonNull(jfGoodsResps.getSeckillInfo()) && Objects.nonNull(jfGoodsResps.getSeckillInfo().getSeckillStartTime()) && (DateTime.now().isBefore(jfGoodsResps.getSeckillInfo().getSeckillStartTime()))) {
        str.append("———").append("\r\n").append("预告：该产品将于【").append(new DateTime(jfGoodsResps.getSeckillInfo().getSeckillStartTime()).toString("yyyy-MM-dd HH:mm:ss")).append("】参与秒杀更优惠！！！");
      }


      //无优惠券但 将来会有拼团购信息
      if (Objects.nonNull(jfGoodsResps.getPinGouInfo()) && Objects.nonNull(jfGoodsResps.getPinGouInfo().getPingouStartTime()) && (DateTime.now().isBefore(jfGoodsResps.getPinGouInfo().getPingouStartTime()))) {
        str.append("———").append("\r\n").append("预告：该产品将于【").append(new DateTime(jfGoodsResps.getPinGouInfo().getPingouStartTime()).toString("yyyy-MM-dd HH:mm:ss")).append("】参与拼团购更优惠！！！");
      }
    }


    sbTemplate.append(AllEnums.ownerEnum.getStr(jfGoodsResps.getOwner())).append(jfGoodsResps.getSkuName())
        .append("\r\n")
        .append("原价:").append(new BigDecimal(jfGoodsResps.getPriceInfo().getPrice().toString())).append("元,")
        .append("券后：").append(totalMoney).append("元")
        .append("\r\n")
        .append(Utils.toHttpUrl(jfGoodsResps.getMaterialUrl())).append("\r\n").append(str);


    return sbTemplate.toString();
//    }

//    return null;
  }
}
