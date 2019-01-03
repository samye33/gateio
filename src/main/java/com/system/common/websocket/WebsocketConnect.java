package com.system.common.websocket;

import com.server.MyConstant;
import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;

public class WebsocketConnect {

    private static Logger logger = Logger.getLogger(WebsocketConnect.class);

    public static void monitorCoin(String coinName) {
        WebSocketClient client = WebsocketConnectionSortTypeByCoinNameTest.monitorCoin(0);
        new Runnable() {
            public void run() {
                for (int i = 0; i < 3; i++) {
                    WebsocketConnectionSortTypeByCoinNameTest.monitorCoin(i);
                }
            }
        };
        long rid = (long) Math.floor(Math.random() * Math.floor(1e7));
        client.send("{\"id\":" + rid + ",\"method\":\"server.ping\",\"params\":[]}");
        client.send("{\"id\":" + rid + ",\"method\":\"depth.subscribe\",\"params\":[\"" + coinName + "\",30,\"0.00000001\"]}");
        client.send("{\"id\":" + rid + ",\"method\":\"trades.subscribe\",\"params\":[\"" + coinName + "\"]}");
        client.send("{\"id\":" + rid + ",\"method\":\"ticker.subscribe\",\"params\":[\"" + coinName + "\",86400]}");
        client.send("{\"id\":" + rid + ",\"method\":\"price.subscribe\",\"params\":[" + MyConstant.global_price_watch_markets() + "]}");
        client.send("{\"id\":" + rid + ",\"method\":\"kline.subscribe\",\"params\":[\"" + coinName + "\",1800]}");
    }
    public static void main(String[] args) {
        monitorCoin("gtc_usdt");
    }

}

