package daniel.biniek.Market;

import daniel.biniek.Controller.Menu;
import daniel.biniek.Controller.ProductController;

import java.util.Scanner;

public class UserInterface {

    private static ProductController productController = new ProductController();

    public static void main(String arg[]) throws Exception {
        Scanner in = new Scanner(System.in);
        String option = new String();
        while (!option.toUpperCase().equals(Menu.EXIT.getName().toUpperCase())) {
            productController.printMenuContent();
            option = in.nextLine();
            Menu menu = productController.convertStringToMenu(option);
            productController.chooseAction(menu);
        }
    }
}
