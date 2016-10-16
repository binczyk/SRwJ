package main.java.daniel.biniek.main;

import main.java.daniel.biniek.JMS.QueueCode;
import main.java.daniel.biniek.product.ProductOb;
import main.java.daniel.biniek.product.ProductSender;
import main.java.jms.MTQueueCode;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.*;

public class BackendInterface {

    public static void main(String arg[]) throws Exception {
        sendProductToQueue();
        initBroker();
        System.out.println(receiveMessage());
    }

    private static void start() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(QueueCode.BACK_TO_MT_QUEUE.toString());
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();
            ProductSender productSender = new ProductSender();
            ProductOb productOb = productSender.randomProduct();
            message.setText("bacend: " + QueueCode.MT_TO_BACK_QUEUE.toString()+ "." + (Math.round(Math.random() * 100)) +
                    " product: " + productOb.getName() + " price: " + productOb.getPrice());
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

    public static void sendProductToQueue() {
        ProductSender productSender = new ProductSender();

        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
            Destination destination = session.createQueue(QueueCode.BACK_TO_MT_QUEUE.toString());
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            ProductOb productOb = productSender.randomProduct();
            productOb.setBackName(QueueCode.MT_TO_BACK_QUEUE.toString() + "." + (Math.round(Math.random() * 100)));
            ObjectMessage message = session.createObjectMessage();
            message.setObject(productOb);
            message.setJMSType("Product");
            message.setStringProperty("WDSR", "ProductProcessor");
            producer.send(message);
            producer.close();
            System.out.println(productOb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void initBroker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:61617");
        broker.start();
    }

}




