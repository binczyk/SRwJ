package main.java.jms;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MTJms {

    public void receiveMessage() {
        try {
            boolean isEmpty = false;
            ConnectionFactory factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(MTQueueCode.BACK_TO_MT_QUEUE.toString());
            MessageConsumer consumer = session.createConsumer(destination);
            do {
                Message message = consumer.receive();

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    isEmpty = textMessage.getText().isEmpty();
                    System.out.println(textMessage.getText());
                } else {
                    isEmpty = true;
                }

            } while (!isEmpty);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
