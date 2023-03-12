package ua.knu.csc.iss.ynortman.utils;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

@Slf4j
public class RabbitUtils {

    private static ConnectionFactory factory;

    public static void send(Object obj, String queueName, boolean purge) throws IOException, TimeoutException {
        ConnectionFactory factory = getFactory();

        try (Connection conn = factory.newConnection()) {
            Channel channel = conn.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);
            if(purge) {
                channel.queuePurge(queueName);
            }
            byte[] messageBodyBytes = SerializationUtils.serialize((Serializable) obj);
            channel.basicPublish("", queueName, null, messageBodyBytes);
            log.debug("Message was sent");
        }
    }

    public static void receive(String queueName, Consumer action) throws IOException, TimeoutException {
        log.debug("RECEIVE METHOD CALL");
        ConnectionFactory factory = getFactory();
        Connection connection = factory.newConnection();
//        connection.addShutdownListener(e -> {
//            e.printStackTrace();
//            log.error(String.valueOf(e.getReason()));
//        });
        Channel channel = connection.createChannel();

        channel.queueDeclare(queueName, true, false, false, null);
        log.debug(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Object obj = SerializationUtils.deserialize(delivery.getBody());
                log.debug(" [x] Received ");
                action.accept(obj);
            connection.close();
//                try {
//                    connection.close();
//                } catch (Exception e) {
////                    e.printStackTrace();
//                    log.error(e.getMessage());
//                }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }

    private static ConnectionFactory getFactory() {
        if(factory == null) {
            factory = new ConnectionFactory();
            factory.setHost(PropertyUtils.getProperty("rabbitmq.host"));
            factory.setPort(Integer.parseInt(PropertyUtils.getProperty("rabbitmq.port")));
            factory.setUsername(PropertyUtils.getProperty("rabbitmq.username"));
            factory.setPassword(PropertyUtils.getProperty("rabbitmq.password"));
            factory.setAutomaticRecoveryEnabled(true);
        }
        return factory;
    }
}
