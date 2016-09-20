package daniel.biniek.JMS;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

public class QueueFactory {

    private static final String URL = "tcp://localhost:61616";

    public void start() throws Exception {
        BrokerService broker = new BrokerService();
        broker.setPersistent(false);
        broker.addConnector("tcp://0.0.0.0:61616");
        broker.start();

        ActiveMQConnectionFactory activeMQConnectionFactory  =  new ActiveMQConnectionFactory(URL);
        Connection connection =  activeMQConnectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QueueCode.BACK_TO_MT_QUEUE.name());
        MessageConsumer consumer = session.createConsumer(queue);
    }

}
