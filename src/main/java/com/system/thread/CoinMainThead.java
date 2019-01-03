package com.system.thread;

import com.server.MyConstant;
import com.utils.HttpBase;
import com.system.common.websocket.WebsocketConnectionSortTypeByCoinName;
import org.apache.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class CoinMainThead extends Thread {


    private Logger logger = Logger.getLogger(this.getClass());


    public void run() {
        WebDriver dr = null;
        try {
            dr = HttpBase.getChromeDriver();
            dr.manage().window().maximize();
            dr.get("https://gateio.io");
            Thread.sleep(1 * 60 * 1000);
            getCookie(dr);

            //MyConstant.COINDTYPE=coin_name,coin_precision_rate,buyPrice,expectPrice,quantity|
            /**
             * @coin_precision_rate 小数点后面有几位
             * @expectPrice期待卖出的最低价格
             * @quantity 该币的数量
             * */
            String[] coins_type = MyConstant.COINTYPE.split("\\|");
            String[][] coins = new String[coins_type.length][4];
            String monitor_price_markets = "";
            //对数组初始化
            for (int i = 0; i < coins_type.length; i++) {
                String[] coin_name = coins_type[i].split(",");
                if (i == coins_type.length - 1) {
                    monitor_price_markets += "\"" + coin_name[0] + "\"";
                } else {
                    monitor_price_markets += "\"" + coin_name[0] + "\"" + ",";
                }
                for (int j = 0; j < coin_name.length; j++) {
                    coins[i][j] = coin_name[j];
                }
            }
            MyConstant.MONITOR_GLOBAL_PRICE_MARKETS = monitor_price_markets;
            //创建一个websocket的连接
            WebsocketConnectionSortTypeByCoinName.monitorCoin(coins[0][0]);

            ExecutorService pool = Executors.newFixedThreadPool(coins_type.length);
            List<Future> list = new ArrayList<Future>();
            for (int i = 0; i < coins_type.length; i++) {
                logger.error("币种" + coins[i][0] + "正在监测");
                Callable c = new CoinReceiveThread(coins[i][0], Integer.parseInt(coins[i][1]), Double.parseDouble(coins[i][2]), Double.parseDouble(coins[i][3]), i);
                // 执行任务并获取Future对象
                Future f = pool.submit(c);
//                Future f1 = pool.submit(c1);
                list.add(f);
            }
            // 关闭线程池
            pool.shutdown();
            // 获取所有并发任务的运行结果
            for (Future f : list) {
                System.out.println(">>>" + f.get().toString());
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {
                Runtime.getRuntime().exec("cmd.exe /c start taskkill /f /im chromedriver.exe");
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    private void getCookie(WebDriver dr) {
        //刷新页面
        dr.navigate().refresh();
        //获取cookie
        Set cookies = dr.manage().getCookies();
        String aaa = "";
        MyConstant.COOKIEBROWSER = cookies.toString();
        for (Iterator it = cookies.iterator(); it.hasNext(); ) {
            Object next = it.next();
            Cookie a = ((Cookie) next);
            aaa = aaa + a.getName() + "=" + a.getValue() + ";";
            if (a.getName().equals("AWSALB")) {
                MyConstant.AWSALB = a.getValue();
            } else if (a.getName().equals("pver")) {
                MyConstant.PVER = a.getValue();
            } else if (a.getName().equals("uid")) {
                MyConstant.UID = a.getValue();
            } else if (a.getName().equals("captcha")) {
                MyConstant.CAPTCHA = a.getValue();
            } else if (a.getName().equals("nickname")) {
                MyConstant.NICKNAME = a.getValue();
            }
        }
        MyConstant.COOKIETRADEBID = aaa;
    }

}
