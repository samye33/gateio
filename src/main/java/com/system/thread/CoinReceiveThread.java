package com.system.thread;

import com.alibaba.fastjson.JSONArray;
import com.server.MyConstant;
import com.system.common.activeMQ.Activemq;
import com.utils.*;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

public class CoinReceiveThread implements Callable<Object> {

    private static Logger logger = Logger.getLogger(CoinReceiveThread.class);

    private String coinName;

    private int precision_rate;

    private double expectPrice;
    private double quantity;
    private int thread_num;
    private static DateFormat Data_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private double prePrice;
    private long pre_time;
    private long http_pre_time;
    private double growthRate;
    private double preGrowthRate;
    private double maxPrice = 0;
    private int num = 1;
    private int decline_num = 1;
    private static String referer;

    protected CoinReceiveThread(String coinName, int precision_rate, double expectPrice, double quantity, int thread_num) {
        this.coinName = coinName;
        this.precision_rate = precision_rate;
        this.expectPrice = expectPrice;
        this.quantity = quantity;
        this.thread_num = thread_num;
        this.referer = "https://gateio.io/trade/" + coinName;
    }

    public Object call() throws Exception {
        MessageConsumer consumer = Activemq.messageConsumer(coinName);
        try {
            consumer.setMessageListener(new MessageListener() {
                //                @Override
                public void onMessage(Message message) {
                    TextMessage textMessage = (TextMessage) message;
                    long c_time = new Date().getTime();
                    try {
                        JSONArray jsonArray = JSONArray.parseArray(textMessage.getText());
                        double currentPrice = 0;
                        try {
                            currentPrice = Double.parseDouble(jsonArray.getJSONObject(1).getString("price"));
                        } catch (Exception e) {
                            logger.error("", e);
                        }
                        if (currentPrice > maxPrice) {
                            maxPrice = currentPrice;
                        }
                        if (num == 1) {
                            http_pre_time = c_time;
                            prePrice = currentPrice;
                            pre_time = c_time;
                            num++;
                        }
                        if (currentPrice == 0.0) {
                        } else {
                            if (c_time - pre_time > 10000) {
                                long did = (long) Math.floor(Math.random() * Math.floor(1e7));
                                String url_cookie = "https://gateio.io/json_svr/query/?u=13&c=" + did + "&type=order_list&symbol=" + coinName;
                                if (c_time - http_pre_time > (1000 * 60 * 30)) {
                                    HttpClientTradeGet.getCookie(url_cookie);
                                    http_pre_time = c_time;
                                }
                                //计算增长率
                                growthRate = Arithmetic.calculateGrowthRate(currentPrice, prePrice, (c_time - pre_time) / 1000);
                                if (num == 2) {
                                    preGrowthRate = growthRate;
                                    num++;
                                }
                                //返回出售额度，如果返回值为负(如果为-2.0)，则表示快出售，如果为0则表示不出售，为正值，则以该价格出售
                                double salePrice = Arithmetic.salePrice(maxPrice, growthRate, preGrowthRate, currentPrice, expectPrice, coinName);
                                if (salePrice == -2.0) {
                                    if (decline_num >= 3) {
                                        logger.info(coinName + "以当前价格的99.9%出售，价格为：" + currentPrice * 0.999);
                                        long cid = (long) Math.floor(Math.random() * Math.floor(1e7));
                                        String url = "https://gateio.io/json_svr/exchange/?u=1&c=" + cid + "&type=bid&symbol=" + coinName + "&rate=" + 500 + "&vol=" + 1 + "&fundpass=" + MyConstant.TRADEPWD + "&captcha=";
//                                        String url = "https://gateio.io/json_svr/exchange/?u=1&c=" + cid + "&type=bid&symbol=" + coinName + "&rate=" + (currentPrice * 0.999) + "&vol=" + quantity + "&fundpass=" + MyConstant.TRADEPWD + "&captcha=";
                                        Boolean saleFlag = HttpClientTradeGet.saleCoin(url, referer);
                                    }
                                    decline_num++;
                                } else if (salePrice == 0.0) {
                                    if (decline_num != 0) {
                                        decline_num--;
                                    }

                                } else if (salePrice == -1.0) {

                                } else if (salePrice == -3.0) {

                                } else {
                                    long cid = (long) Math.floor(Math.random() * Math.floor(1e7));
                                    String url = "https://gateio.io/json_svr/exchange/?u=1&c=" + cid + "&type=bid&symbol=" + coinName + "&rate=" + 500 + "&vol=" + 1 + "&fundpass=" + MyConstant.TRADEPWD + "&captcha=";
//                                    String url = "https://gateio.io/json_svr/exchange/?u=1&c=" + cid + "&type=bid&symbol=" + coinName + "&rate=" + (salePrice * 0.999) + "&vol=" + quantity + "&fundpass=" + MyConstant.TRADEPWD + "&captcha=";
                                    Boolean saleFlag = HttpClientTradeGet.saleCoin(url, referer);
                                    logger.info(coinName + "以当前价格的99.9%出售，价格为：" + salePrice);
                                }
                                preGrowthRate = growthRate;
                                prePrice = currentPrice;
                                pre_time = c_time;
                            }
                        }
                    } catch (Exception e1) {
                        logger.error("", e1);
                    }

                }
            });
        } catch (Exception e2) {
            logger.error("", e2);
        }
        return null;
    }
}
