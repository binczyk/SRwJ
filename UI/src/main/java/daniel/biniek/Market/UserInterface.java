package daniel.biniek.Market;

import daniel.biniek.Controller.Menu;
import daniel.biniek.Controller.ProductController;
import daniel.biniek.Controller.Receiver;

import java.util.Scanner;

public class UserInterface {

    private static ProductController productController = new ProductController();

    public static void main(String arg[]) throws Exception {
        thread(new Receiver(), false);
        Scanner in = new Scanner(System.in);
        String option = new String();
        while (!option.toUpperCase().equals(Menu.EXIT.getName().toUpperCase())) {
            productController.printMenuContent();
            option = in.nextLine();
            Menu menu = productController.convertStringToMenu(option);
            productController.chooseAction(menu);
        }
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
}
