package daniel.biniek.Product;

import org.apache.activemq.broker.BrokerService;

import javax.xml.ws.Endpoint;

public class Publisher {

    public static void main(String[] args) throws Exception {
        Endpoint.publish("http://localhost:8080/test", new ProductServiceImpl());
        System.out.println("endpoint stated");

        //initBroker();
        //System.out.println("broker started");
    }

    private static void initBroker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:61616");
        broker.start();
    }

}

