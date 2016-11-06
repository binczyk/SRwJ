package daniel.biniek.Controller;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.*;

public class Notification implements Runnable{

    private static final String URL = "vm://localhost:61666";
    private static final String TOPIC = "NOTIFICATION";

    public void run() {
        String notification = "TEST TEST TEST";
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
            Destination destination = session.createQueue(TOPIC);
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();
            message.setText(notification);
            producer.send(message);
            System.out.println("Sent: " + message.getText());
            session.close();
            connection.close();
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
