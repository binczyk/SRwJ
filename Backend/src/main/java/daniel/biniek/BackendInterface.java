package daniel.biniek;

import daniel.biniek.JMS.QueueCode;
import daniel.biniek.product.ProductOb;
import daniel.biniek.product.ProductSender;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.*;
import java.net.ServerSocket;

public class BackendInterface {

    public static void main(String arg[]) throws Exception {
        sendProductToQueue(arg[0]);
        initBroker();
    }

    private static String receiveMessage() {
        String products = new String();
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(QueueCode.MT_TO_BACK_QUEUE.toString());
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

    public static void sendProductToQueue(String backNo) {
        ProductSender productSender = new ProductSender();

        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            connectionFactory.setTrustAllPackages(true);
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
            Destination destination = session.createQueue(QueueCode.BACK_TO_MT_QUEUE.toString());
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            ProductOb productOb = productSender.randomProduct();
            productOb.setBackName(QueueCode.MT_TO_BACK_QUEUE.toString() + "." + backNo);
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
        ServerSocket socket = new ServerSocket(0);
        BrokerService broker = new BrokerService();
        socket.close();
        String bindAddress = "tcp://localhost:" + socket.getLocalPort();
        broker.addConnector(bindAddress);
        broker.start();
    }

}




