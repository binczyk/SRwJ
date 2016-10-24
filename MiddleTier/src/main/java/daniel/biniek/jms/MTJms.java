package daniel.biniek.jms;

import daniel.biniek.product.ProductOb;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MTJms {

    private ActiveMQConnectionFactory connectionFactory;

    public List<ProductOb> receiveMessage() throws JMSException {
        ActiveMQConnection connection;
        Queue queue;
        ArrayList<ProductOb> productObs = new ArrayList<>();
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            factory.setTrustAllPackages(true);
            connection = (ActiveMQConnection) factory.createConnection();

            QueueSession session = connection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
            queue = session.createQueue(MTQueueCode.BACK_TO_MT_QUEUE.toString());
            connection.start();
            QueueBrowser browser = session.createBrowser(queue);
            Enumeration<?> messagesInQueue = browser.getEnumeration();

            while (messagesInQueue.hasMoreElements()) {
                ObjectMessage objectMessage = (ObjectMessage) messagesInQueue.nextElement();
                if (objectMessage.getObject() instanceof ProductOb) {
                    ProductOb product = (ProductOb) objectMessage.getObject();
                    System.out.println(product.toString());
                    productObs.add(product);
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
           /* connection.stop();
            connection.destroyDestination(ActiveMQDestination.transform(queue));*/
            return productObs;
        }
    }

    public void sendMessages(String mse, String productName, String backendName) {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(backendName);
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();
            message.setText(productName + ";" + mse);
            producer.send(message);
            System.out.println("Sent: " + message.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
