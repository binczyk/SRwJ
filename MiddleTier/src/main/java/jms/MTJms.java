package main.java.jms;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.util.Enumeration;

public class MTJms {

    public String receiveMessage() {
        String products = new String();

        try {
            boolean isEmpty = false;
            ConnectionFactory factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            ActiveMQConnection connection = (ActiveMQConnection) factory.createConnection();
            connection.start();
            QueueSession session = connection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
            Queue queue = session.createQueue(MTQueueCode.BACK_TO_MT_QUEUE.toString());
            QueueBrowser browser = session.createBrowser(queue);
            Enumeration<?> messagesInQueue = browser.getEnumeration();
           // Destination destination = session.createQueue(MTQueueCode.BACK_TO_MT_QUEUE.toString());
           // MessageConsumer consumer = session.createConsumer(destination);
            while (messagesInQueue.hasMoreElements()) {
                Message queueMessage = (Message) messagesInQueue.nextElement();
                System.out.println(((ActiveMQTextMessage) queueMessage).getText());
                products += ((ActiveMQTextMessage) queueMessage).getText() + "\n";
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
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


}
