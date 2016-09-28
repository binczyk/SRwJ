package daniel.biniek.Product;

import javax.xml.ws.Endpoint;

public class Publisher {

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/test", new ProductServiceImpl());
        System.out.println("end");
    }
}

