package daniel.biniek.UI.backend;

import daniel.biniek.JMS.QueueFactory;
import daniel.biniek.ProductImpl.Product;
import daniel.biniek.ProductImpl.ProductSender;

import java.util.ArrayList;
import java.util.List;

public class BackendInterface {


    public static void main(String arg[]) throws Exception {

        QueueFactory queueFactory = new QueueFactory();

        queueFactory.start();

        ProductSender productSender = new ProductSender();
        List<Product> products = new ArrayList<>();

        products = productSender.randomProducts();

    }



}




