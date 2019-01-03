package com.utils;

import com.server.MyConstant;
import org.apache.log4j.Logger;


public class Arithmetic{

    private static Logger logger = Logger.getLogger(Arithmetic.class);

    public static double calculateGrowthRate(double y1, double y0, long x) {
        Double rate = (y1 - y0) / x;
        return rate;
    }

    public static Double salePrice(Double maxPrice, Double currentRate, Double preRate, Double currentPrice, Double expectVal, String coin_name) {
        Double retVal = 0.0;

        if (currentRate > 0) {
            if (currentRate - preRate > 0) {
                logger.info(coin_name + "增长率在涨；这次的增长率为：" + currentRate + "上一次的增长率为：" + preRate + "价格为:" + currentPrice);
            } else if (currentRate - preRate == 0) {
            } else {
                logger.info(coin_name + "再涨，但是增长率变小了；这次的增长率为：" + currentRate + "上一次的增长率为：" + preRate + "价格为:" + currentPrice);
            }
            retVal = 0.0;
        } else if (currentRate < 0) {
            if (currentPrice <= (maxPrice * MyConstant.SALERATE) && currentPrice > expectVal) {
                logger.info("该出售了");
                retVal = maxPrice * 0.9 * 0.999;
            } else {
                if (currentRate < 0) {
                    if (preRate >= 0) {
                        logger.info(coin_name + "在跌了，需要注意；这次的增长率为：" + currentRate + "上一次的增长率为：" + preRate + "价格为:" + currentPrice);
                        retVal = -1.0;
                    } else {
                        if (currentRate < preRate) {
                            logger.info(coin_name + "在大幅度的跌了，跌的速率比之前还快；这次的增长率为：" + currentRate + "上一次的增长率为：" + preRate + "价格为:" + currentPrice);
                            retVal = -2.0;
                        } else if (currentRate == preRate) {
                            retVal = 0.0;
                        } else {
                            logger.info(coin_name + "跌的速率变慢了，可能在回升，需要注意;这次的增长率为：" + currentRate + "上一次的增长率为：" + preRate + "价格为:" + currentPrice);
                            retVal = -3.0;
                        }
                    }
                }
            }
        } else {
            retVal = 0.0;
        }
        return retVal;
    }


}
