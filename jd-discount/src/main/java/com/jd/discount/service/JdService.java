package com.jd.discount.service;

/**
 * @author zf
 * since 2019/12/21
 */
public interface JdService {
  boolean setMsgToRedis(String content,String imageName);
}
