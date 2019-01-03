package com.utils;

import com.server.MyConstant;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpClientTradeGet {

    private static Logger logger = Logger.getLogger(HttpClientTradeGet.class);

    public static Boolean saleCoin(String url, String referer) {
        Map map = new HashMap();
        map.put("cookie", MyConstant.InitialCookieBySale());
        //"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0"
        map.put("user-agent", MyConstant.USERAGENT);
        map.put("referer", referer);
        try {
            String html = HttpBase.get(url, "utf-8", map).getResult();
            logger.info("卖出后的状态" + html);
        } catch (Exception e) {
            logger.error("", e);
        }
        return false;
    }

    public static void getCookie(String url) {
        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        try {
            client.executeMethod(getMethod);
            Cookie[] cookies = client.getState().getCookies();
            String aaa = "";
            for (int i = 0; i < cookies.length; i++) {
                aaa = aaa + cookies[i].toString();
            }
            try {
                MyConstant.AWSALB = aaa.replace("AWSALB=", "");
            } catch (Exception e) {
            }
            getMethod.releaseConnection();
        } catch (IOException e) {
        }

    }

    public static void main(String[] args) {
        String url = "https://gateio.io/json_svr/exchange/?u=1&c=820378&type=bid&symbol=GTC_USDT&rate=1000.218&vol=1.727&fundpass=01222018&captcha=";
        String referer = "https://gateio.io/trade/GTC_USDT";
        saleCoin(url, referer);
    }
}
