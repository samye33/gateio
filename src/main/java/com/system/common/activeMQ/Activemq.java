package com.system.common.activeMQ;

import com.utils.JMSConnectionUtill;
import org.apache.log4j.Logger;

import javax.jms.*;

public class Activemq {
    private static Logger logger = Logger.getLogger(Activemq.class);

    public static Connection connection = null;

    public static Session session = null;

    public static void connection() {
        connection = JMSConnectionUtill.getInstance();
    }

    public static Session session() {
        try {
            if (connection == null) {
                connection();
            }
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (Exception e) {
            logger.error("", e);
        }
        return session;
    }

    public static MessageProducer messageProducer(String queueName) {
        MessageProducer producer = null;
        try {
            if (session == null) {
                session();
            }
            Destination destination = null;
            try {
                destination = session.createQueue(queueName);
            } catch (Exception e) {
                connection.close();
                session.close();
                connection = null;
                Thread.sleep(3000);
                session();
                destination = session.createQueue(queueName);
            }
            producer = session.createProducer(destination);
        } catch (Exception e) {
            logger.error("", e);
        }
        return producer;
    }

    public static MessageConsumer messageConsumer(String queueName) {
        MessageConsumer consumer = null;
        try {
            if (session == null) {
                session();
            }
            Destination destination = null;
            try {
                destination = session.createQueue(queueName);
            } catch (Exception e) {
                connection.close();
                session.close();
                connection = null;
                Thread.sleep(3000);
                session();
                destination = session.createQueue(queueName);
            }
            consumer = session.createConsumer(destination);
        } catch (Exception e) {
            logger.error("", e);
        }
        return consumer;
    }
}
