package com.system.common.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.server.MyConstant;
import com.system.common.activeMQ.Activemq;
import com.utils.CommonUtils;
import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.net.URI;

public class WebsocketConnectionSortTypeByCoinName {

    private static Logger logger = Logger.getLogger(WebsocketConnectionSortTypeByCoinName.class);
    private static WebSocketClient client;
    private static int num = 0;

    public static void monitorCoin(final String coinName) {
        try {

            double random = Math.random();
            String url = "wss://webws.gateio.io/v3/?k=30&v=" + random;
            client = new WebSocketClient(new URI(url)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    logger.info("握手成功");
                }

                @Override
                public void onMessage(String msg) {
                    JSONObject jsonObject = JSON.parseObject(msg);
                    boolean flag = false;
                    try {
                        flag = jsonObject.getString("method").equals("price.update");
                    } catch (Exception e) {

                    }
                    if (flag) {
                        String coin_name = CommonUtils.upperStr(jsonObject.getJSONArray("params").get(0).toString());
                        if (MyConstant.COINTYPE.contains(coin_name)) {
                            Session session = Activemq.session();
                            MessageProducer producer = Activemq.messageProducer(coin_name);
                            producer = Activemq.messageProducer(coin_name);
                            TextMessage textMessage = null;
                            try {
                                textMessage = session.createTextMessage(jsonObject.getJSONArray("params").toString());
                                producer.send(textMessage);
                            } catch (JMSException e) {
                                logger.error("", e);
                            }
                        }
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    logger.info("websocket已关闭；i为:" + i + " ；s为:" + s + " ；b为" + b);
                    client.close();
                    client = null;
                }

                @Override
                public void onError(Exception e) {
                    client = null;
                    logger.error("发生错误已关闭", e);
                }
            };
        } catch (Exception e) {
            logger.error("", e);
        }
        client.connect();
        while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        //连接成功,发送信息
        logger.info("Connected");
        long rid = (long) Math.floor(Math.random() * Math.floor(1e7));
        client.send("{\"id\":" + rid + ",\"method\":\"server.ping\",\"params\":[]}");
        client.send("{\"id\":" + rid + ",\"method\":\"depth.subscribe\",\"params\":[\"" + coinName + "\",30,\"0.00000001\"]}");
        client.send("{\"id\":" + rid + ",\"method\":\"trades.subscribe\",\"params\":[\"" + coinName + "\"]}");
        client.send("{\"id\":" + rid + ",\"method\":\"ticker.subscribe\",\"params\":[\"" + coinName + "\",86400]}");
        client.send("{\"id\":" + rid + ",\"method\":\"price.subscribe\",\"params\":[" + MyConstant.MONITOR_GLOBAL_PRICE_MARKETS + "]}");
        client.send("{\"id\":" + rid + ",\"method\":\"kline.subscribe\",\"params\":[\"" + coinName + "\",1800]}");
        if (num > 30) {
            num = 0;
        } else {
            num++;
        }

    }


}

