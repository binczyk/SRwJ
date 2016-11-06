package daniel.biniek.Market;

import daniel.biniek.Controller.Menu;
import daniel.biniek.Controller.ProductController;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import java.util.Scanner;

public class UserInterface {

    private static ProductController productController = new ProductController();
    private static final String URL = "vm://localhost:61666";


    public static void main(String arg[]) throws Exception {
        initBroker();

        Scanner in = new Scanner(System.in);
        String option = new String();
        while (!option.toUpperCase().equals(Menu.EXIT.getName().toUpperCase())) {
            productController.printMenuContent();
            option = in.nextLine();
            Menu menu = productController.convertStringToMenu(option);
            productController.chooseAction(menu);
        }
    }

    private static void initBroker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.setPersistent(false);
        broker.addConnector(URL);
        broker.start();
    }
}
