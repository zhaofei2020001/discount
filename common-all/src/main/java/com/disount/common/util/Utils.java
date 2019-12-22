package com.disount.common.util;

import com.disount.common.constant.Constants;
import org.joda.time.DateTime;

/**
 * @author zf
 * since 2019/12/21
 */
public class Utils {

  public static  String getAllImagePath(String url ){
    return Constants.CONF_ASSETS_DIR_DEFAULT+ DateTime.now().toString("yyyy")+"/"+ DateTime.now().toString("MM")+"/"+ DateTime.now().toString("dd")+"/"+url;
  }
}
