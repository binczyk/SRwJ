package daniel.biniek.Controller;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class Receiver implements ExceptionListener, Runnable {

    private static final String URL = "vm://localhost:61666";
    private static final String TOPIC = "NOTIFICATION";


    public void run(){
        List<String> messages = new ArrayList<>();
        TopicConnectionFactory connectionFactory;
        TopicConnection connection;
        TopicSession session;
        Topic destination;
        try {
            connectionFactory = new ActiveMQConnectionFactory(URL);
            connection = connectionFactory.createTopicConnection();
            connection.start();
            session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic(TOPIC);

            MessageConsumer consumer = session.createSubscriber(destination);
           // consumer.setMessageListener(new ConsumerMessageListener());
            Message message = consumer.receive(1000);
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                messages.add(text);
                System.out.println("class Received: " + text);
            } else {
                System.out.println("class Received: " + message);
            }

            consumer.close();
            session.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void onException(JMSException e) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }

    private static void initBroker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector(URL);
        broker.start();
    }
}
