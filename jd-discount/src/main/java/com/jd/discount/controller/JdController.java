package com.jd.discount.controller;

import com.jd.discount.service.JdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zf
 * since 2019/12/21
 */
@RestController
@RequestMapping("/jd")
public class JdController {
  @Autowired
  JdService jdService;

  @GetMapping("/set/robot/msg")
  public boolean setMsgToRedis(@RequestParam("content") String content,@RequestParam("imgName") String imgName) {
    return jdService.setMsgToRedis(content,imgName);
  }
}
