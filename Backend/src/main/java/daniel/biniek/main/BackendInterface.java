package main.java.daniel.biniek.main;

import main.java.daniel.biniek.product.ProductOb;
import main.java.daniel.biniek.product.ProductSender;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.Serializable;

public class BackendInterface {

    public static void main(String arg[]) throws Exception {
        start();
    }

    private static void start() {
        try {
            ProductSender productSender = new ProductSender();
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("BACK_TO_MT_QUEUE");
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();
            ProductOb productOb= productSender.randomProduct();
            message.setText("product: " + productOb.getName() + " price: " + productOb.getPrice());
            producer.send(message);
            System.out.println("Sent: " + message.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}




