package com.system.common.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.server.MyConstant;
import com.utils.CommonUtils;
import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebsocketConnectionSortTypeByCoinNameTest {

    private static Logger logger = Logger.getLogger(WebsocketConnectionSortTypeByCoinNameTest.class);
    private static WebSocketClient client;

    public static WebSocketClient monitorCoin(final int i) {
        try {
            double random = Math.random();
            String url = "wss://webws.gateio.io/v3/?k=30&v=" + random;
            System.out.println(url);
            client = new WebSocketClient(new URI(url)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("握手成功");
                }

                @Override
                public void onMessage(String msg) {
                    JSONObject jsonObject = JSON.parseObject(msg);
                    System.out.println("我是线程" + i+";消息是：" + msg);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("websocket已关闭；i为:" + i + " ；s为:" + s + " ；b为" + b);
                    System.out.println("链接已关闭");
                }

                @Override
                public void onError(Exception e) {
                    System.out.println("发生错误已关闭");
                }
            };
        } catch (Exception e) {
            logger.error("", e);
        }
        client.connect();
        logger.info(client.getDraft());
        while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //连接成功,发送信息
        System.out.println("Connected");
        return client;
    }



}

