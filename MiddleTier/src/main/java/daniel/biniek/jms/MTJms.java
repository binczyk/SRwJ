package daniel.biniek.jms;

import daniel.biniek.product.ProductOb;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Enumeration;

public class MTJms {

    private ActiveMQConnectionFactory connectionFactory;

    public String receiveMessage() throws JMSException {
        String products = new String();
        ActiveMQConnection connection = null;
        Queue queue = null;
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
                    System.out.println(product.getName() + " " + product.getPrice() + " " + product.getBackName());
                    products += product.getName() + " " + product.getPrice() + " " + product.getBackName() + "\n";
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
           /* connection.stop();
            connection.destroyDestination(ActiveMQDestination.transform(queue));*/
            return products;
        }
    }

    public void sendMessages(String mse) {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("MT_TO_BACK_QUEUE");
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();
            message.setText(mse);
            producer.send(message);
            System.out.println("Sent: " + message.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

/*
    public void sendProductToQueue(ProductOb product) {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            connection.start();
            Destination destination = session.createQueue(QueueCode.MT_TO_BACK_QUEUE.toString());
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            ObjectMessage message = session.createObjectMessage(product);
            message.setJMSType("Order");
            message.setStringProperty("WDSR-System", "OrderProcessor");
            producer.send(message);
            producer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


}
