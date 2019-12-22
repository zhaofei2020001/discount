package com.disount.common.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 精选商品
 * @author zf
 * @since 2019-12-14
 */
@Data
public class JxGoods {

    private int id;
    /**
     * 商品ID
     */
    private Long skuId;


    /**
     * 拼购价格
     */
    private BigDecimal pingouPrice;
    /**
     * 拼购人数
     */
    private Long pingouTmCount;
    /**
     *拼购落地页url
     */
    private String pingouUrl;
    /**
     *拼购开始时间(时间戳，毫秒)
     */
    private Long pingouStartTime;
    /**
     * 拼购结束时间(时间戳，毫秒)
     */
    private Long pingouEndTime;

    /**
     * 频道id：1-好券商品,2-超级大卖场,10-9.9专区,
     * 22-热销爆品,24-数码家电,25-超市,26-母婴玩具,
     * 27-家具日用,28-美妆穿搭,29-医药保健,30-图书文具,
     * 31-今日必推,32-王牌好货,33-秒杀商品,34-拼购商品
     */
    private int eliteId;
    private String eliteName;
    /**
     * 30天引单数量
     */
    private Long inOrderCount30Days;
    /**
     * 30天引单数量(sku维度)
     */
    private Long inOrderCount30DaysSku;
    /**
     * 商品价格
     */
    private BigDecimal price;
    /**
     * 佣金比例
     */
    private Double commissionShare;
    /**
     * 佣金
     */
    private BigDecimal commission;

    /**
     *秒杀价原价
     */
    private BigDecimal seckillOriPrice;
    /**
     *秒杀价
     */
    private BigDecimal seckillPrice;
    /**
     *秒杀开始时间(时间戳，毫秒)
     */
    private Long seckillStartTime;
    /**
     * 秒杀结束时间(时间戳，毫秒)
     */
    private Long seckillEndTime;

//    /**
//     * 优惠券列表信息，返回内容为空说明该SKU无可用优惠券
//     */
//    private List<Coupon> couponList;
    /**
     * 商品好评率
     */
    private Double goodCommentsShare;
    /**
     * 图片信息
     */
//    private List<String> url= Lists.newArrayList();

//    /**
//     * 价格信息
//     */
//    private int priceInfoId;
    /**
     * 店铺id
     */
    private int shopInfoId;
    /**
     * 店铺名称
     */
    private String shopName;

}
