package daniel.biniek.JMS;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.*;

public class Notification {

    private static final String URL = "tcp://localhost";
    private static final String TOPIC = "NOTIFICATION";


    public void sendNotification(String notification) throws Exception {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(TOPIC);
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();
            message.setText(notification);
            producer.send(message);
            System.out.println("Sent: " + message.getText());
            System.out.println(notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initBroker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector(URL);
        broker.start();
    }

}
