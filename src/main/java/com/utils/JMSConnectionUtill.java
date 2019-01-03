package com.utils;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public class JMSConnectionUtill {
    private static Logger logger = Logger.getLogger(JMSConnectionUtill.class);

    public static final String url = "tcp://127.0.0.1:61616";

    private static class CreateConnUtil {
        private static JMSConnectionUtill instance = new JMSConnectionUtill();
    }

    private PooledConnectionFactory pooledConnectionFactory = null;

    private JMSConnectionUtill() {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ActiveMQConnectionFactory connectionFactory = null;
        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        connectionFactory = new ActiveMQConnectionFactory("admin", "admin", url);
        pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        //设置最大连接数
        pooledConnectionFactory.setMaxConnections(200);
        //设置最小
        pooledConnectionFactory.setMaximumActiveSessionPerConnection(50);
        //后台对象清理时，休眠时间超过了3000毫秒的对象为过期
        pooledConnectionFactory.setTimeBetweenExpirationCheckMillis(10000);
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = pooledConnectionFactory.createConnection();
        } catch (JMSException e) {
            logger.error("", e);
        }
        return connection;
    }

    public static final Connection getInstance() {
        Connection connection = CreateConnUtil.instance.getConnection();
        try {
            connection.start();
        } catch (JMSException e) {
            logger.error("", e);
        }
        return connection;
    }

    public static void main(String []agrs){
        Connection connection=getInstance();
        System.out.println(connection);
    }

}
