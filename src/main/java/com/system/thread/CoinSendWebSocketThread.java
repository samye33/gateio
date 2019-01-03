package com.system.thread;

import com.server.MyConstant;
import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;

public class CoinSendWebSocketThread implements Runnable {

    private static Logger logger = Logger.getLogger(CoinSendWebSocketThread.class);

    private String coinName;

    private WebSocketClient client;

    private int precision_rate;

    protected CoinSendWebSocketThread(String coinName, WebSocketClient client, int precision_rate) {
        this.coinName = coinName;
        this.client = client;
        this.precision_rate = precision_rate;
    }
/*
    public Object call() throws Exception {
        String url = "https://gateio.io/trade/" + coinName + "?my_custom";
        Boolean flag = false;
        long rid = (long) Math.floor(Math.random() * Math.floor(1e7));
        while (!flag) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                logger.error("", e);
            }
            client.send("{\"id\":" + rid + ",\"method\":\"depth.subscribe\",\"params\":[\"" + coinName + "\",30,\"0.00000001\"]}");
            if (precision_rate >= 9) {
                client.send("{\"id\":" + rid + ",\"method\":\"depth.subscribe\",\"params\":[\"" + coinName + "\",30,\"0.00000001\"]}");
            } else {
                client.send("{\"id\":" + rid + ",\"method\":\"depth.subscribe\",\"params\":[\"" + coinName + "\",30,\"0.00000001\"]}");
            }
            client.send("{\"id\":" + rid + ",\"method\":\"trades.subscribe\",\"params\":[\"" + coinName + "\"]}");
            client.send("{\"id\":" + rid + ",\"method\":\"ticker.subscribe\",\"params\":[\"" + coinName + "\",86400]}");
            client.send("{\"id\":" + rid + ",\"method\":\"kline.subscribe\",\"params\":[\"" + coinName + "\",1800]}");
        }
        return flag;
    }*/

    public void run() {
        long rid = (long) Math.floor(Math.random() * Math.floor(1e7));
        try {
            client.send("{\"id\":" + rid + ",\"method\":\"server.ping\",\"params\":[]}");
            client.send("{\"id\":" + rid + ",\"method\":\"depth.subscribe\",\"params\":[\"" + coinName + "\",30,\"0.00000001\"]}");
            client.send("{\"id\":" + rid + ",\"method\":\"trades.subscribe\",\"params\":[\"" + coinName + "\"]}");
            client.send("{\"id\":" + rid + ",\"method\":\"ticker.subscribe\",\"params\":[\"" + coinName + "\",86400]}");
            client.send("{\"id\":" + rid + ",\"method\":\"price.subscribe\",\"params\":[" + MyConstant.global_price_watch_markets + "]}");
            client.send("{\"id\":" + rid + ",\"method\":\"kline.subscribe\",\"params\":[\"" + coinName + "\",1800]}");
            Thread.sleep(2000);
        } catch (Exception e) {
            logger.info("需要重新连接");
        }
    }
}
