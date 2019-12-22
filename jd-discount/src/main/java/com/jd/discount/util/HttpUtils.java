package com.jd.discount.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


@Slf4j
public class HttpUtils {

  /**
   * 发送HttpPost请求
   *
   * @param strURL 服务地址
   * @param params json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
   * @return 成功:返回json字符串<br/>
   */
  public static String post(String strURL, String params) {
    System.out.println("POST 参数-----> "+strURL+" - "+params);
    log.info("\n************* url: {} \n params: {} \n", strURL, params);
    BufferedReader reader = null;
    try {
      URL url = new URL(strURL);// 创建连接
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setDoInput(true);
      connection.setUseCaches(false);
      connection.setInstanceFollowRedirects(true);
      connection.setRequestMethod("POST"); // 设置请求方式
      // connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
      connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
      connection.connect();
      //一定要用BufferedReader 来接收响应， 使用字节来接收响应的方法是接收不到内容的
      OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
      out.append(params);
      out.flush();
      out.close();
      // 读取响应
      reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
      String line;
      String res = "";
      while ((line = reader.readLine()) != null) {
        res += line;
      }
      reader.close();
      return res;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "error"; // 自定义错误信息
  }


  /**
   * HttpsUtil方法https请求结果返回蔚json类型
   * @param Url http请求地址
   * @param Method http请求类型支持POST GET
   * @param Output
   * @return InputStream转换成JSONObject后返回
   * @throws Exception
   */
  public static JSONObject tokenUtil(String Url, String Method, String Output) throws Exception{
    JSONObject jsonObject = null;
    URL conn_url =  new URL(Url);
    HttpURLConnection conn = (HttpsURLConnection)conn_url.openConnection();
    conn.setRequestMethod(Method);
    conn.setReadTimeout(5000);
    conn.setConnectTimeout(5000);
    conn.connect();
    //output获取access_token是不会用到
    if(Output != null){
      OutputStream outputstream =conn.getOutputStream();
      //字符集，防止出现中文乱码
      outputstream.write(Output.getBytes("UTF-8"));
      outputstream.close();
    }
    //正常返回代码为200
    if(conn.getResponseCode()==200){
      InputStream stream = conn.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(stream, "utf-8");
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      String str = null;
      StringBuffer buffer = new StringBuffer();
      while ((str = bufferedReader.readLine()) != null) {
        buffer.append(str);
      }
      bufferedReader.close();
      inputStreamReader.close();
      stream.close();
      conn.disconnect();
      jsonObject = (JSONObject) JSON.parse(buffer.toString());
    }
    System.out.println(conn.getResponseCode());
    return jsonObject;
  }

}