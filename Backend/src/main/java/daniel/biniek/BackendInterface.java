package daniel.biniek;

import daniel.biniek.JMS.QueueCode;
import daniel.biniek.product.ProductOb;
import daniel.biniek.product.ProductSender;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.*;
import java.net.ServerSocket;
import java.util.Enumeration;

public class BackendInterface implements Runnable {

    private static String threadName;

    public static void main(String arg[]) throws Exception {
        ServerSocket socket = new ServerSocket(0);
        threadName = String.valueOf(socket.getLocalPort());
        socket.close();

        BackendInterface backend = new BackendInterface(threadName);
        backend.start();
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
        BrokerService broker = new BrokerService();
        String bindAddress = "tcp://localhost:" + threadName;
        broker.addConnector(bindAddress);
        broker.start();
    }

    public static void receiveMessage(String backNo) throws JMSException {
        ActiveMQConnection connection;
        Queue queue;
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            factory.setTrustAllPackages(true);
            connection = (ActiveMQConnection) factory.createConnection();

            QueueSession session = connection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
            queue = session.createQueue(QueueCode.MT_TO_BACK_QUEUE.toString() + "." + backNo);
            connection.start();
            QueueBrowser browser = session.createBrowser(queue);
            Enumeration<?> messagesInQueue = browser.getEnumeration();

            while (messagesInQueue.hasMoreElements()) {
                Message message = (Message) messagesInQueue.nextElement();
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String[] parts = textMessage.getText().split(";");
                    System.out.println("Method: " + parts[1] + ", product: " + parts[0]);
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
           /* connection.stop();
            connection.destroyDestination(ActiveMQDestination.transform(queue));*/
        }
    }

    @Override
    public void run() {
        sendProductToQueue(threadName);
        try {
            initBroker();
            while (true) {
                System.out.println("Refresh\n");
                receiveMessage(threadName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Starting " + threadName);
        run();
    }

    public BackendInterface(String name) {
        threadName = name;
        System.out.println("Creating " +  threadName);
    }
}




