package com.cst;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Application {

  public static final String OKB = "OKB";
  public static final String SELL = "sell";
  public static final String BUY = "buy";

  public static void main(String[] args) throws IOException {

    new Thread(
            () -> {
              while (true) {
                try {
                  try {
                    showTarget(OKB, BUY, 105.00);
                    showTarget(OKB, SELL, 128.00);
                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                  Thread.sleep(300000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            },
            "t1")
            .start();
  }

  public static void showTarget(String baseCurrency, String side, double triggerMoney)
          throws Exception {
    String url =
            "https://www.okexcn.com/v3/c2c/otc-ticker/quotedPrice?t=%s&baseCurrency=%s&quoteCurrency=CNY&side=%s";
    String okbSell = String.format(url, System.currentTimeMillis() + "", baseCurrency, side);
    String okbSellPrice = getLatestPrice(okbSell);
    System.out.println(baseCurrency + "：" + side + " " + LocalDateTime.now());
    JSONObject jsonArray = new JSONObject(okbSellPrice);
    String result = jsonArray.getJSONArray("data").getJSONObject(0).getString("price") + ", ";
    result += jsonArray.getJSONArray("data").getJSONObject(1).getString("price") + ", ";
    result += jsonArray.getJSONArray("data").getJSONObject(2).getString("price");
    if (side.equals(SELL)
            && Double.parseDouble(jsonArray.getJSONArray("data").getJSONObject(0).getString("price"))
            > triggerMoney) {
      System.err.println("该卖了大于啦");
      Mail.buildAndSendMsg("该卖了大于啦：" + result);
    }
    if (side.equals(BUY)
            && Double.parseDouble(jsonArray.getJSONArray("data").getJSONObject(0).getString("price"))
            < triggerMoney) {
      System.err.println("该买了小于啦");
      Mail.buildAndSendMsg("该买了小于啦：" + result);
    }
    System.out.println(result);
  }

  public static String getLatestPrice(String url) {
    try {
      URL thisUrl = new URL(url); // 把字符串转换为URL请求地址
      HttpURLConnection connection = (HttpURLConnection) thisUrl.openConnection(); // 打开连接
      connection.connect(); // 连接会话
      BufferedReader br =
              new BufferedReader(
                      new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
      String line;
      StringBuilder sb = new StringBuilder();
      while ((line = br.readLine()) != null) { // 循环读取流
        sb.append(line);
      }
      br.close(); // 关闭流
      connection.disconnect(); // 断开连接
      return sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
