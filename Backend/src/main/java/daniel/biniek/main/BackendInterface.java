package main.java.daniel.biniek.main;

import main.java.daniel.biniek.product.ProductOb;
import main.java.daniel.biniek.product.ProductSender;
import main.java.jms.MTQueueCode;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.io.Serializable;
import java.util.Enumeration;

public class BackendInterface {

    private static Long backendId = 0l;

    public static void main(String arg[]) throws Exception {
        start();
        initBroker();
        System.out.println(receiveMessage());
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
            message.setText("bacend: " + backendId + " product: " + productOb.getName() + " price: " + productOb.getPrice());
            producer.send(message);
            System.out.println("Sent: " + message.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private static String receiveMessage() {
        String products = new String();
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(MTQueueCode.MT_TO_BACK_QUEUE.toString());
            MessageConsumer consumer = session.createConsumer(destination);
            Message message = consumer.receive();

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println(textMessage.getText());
            }


        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            return products;
        }
    }

    private static void initBroker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:61617");
        broker.start();
    }

}




