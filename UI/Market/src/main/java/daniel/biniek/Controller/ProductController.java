package daniel.biniek.Controller;

import daniel.biniek.Request.SOAPClient;

import java.io.BufferedReader;
import java.io.FileReader;

public class ProductController {

    private static String CHOOSE_OPTION = "Choose option:";
    private static String BAD_CHOOICE = "It's a bad choice. Try these:";

    private static String getProducts(String soapAction) throws Exception {
        SOAPClient.start();
        return "";
    }

    private static String getFileContent(String path){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String str;
            while ((str = in.readLine()) != null){
                sb.append(str);
            }
            in.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return sb.toString();
    }

    public static void chooseAction(Menu menu) throws Exception {
        switch (menu) {
            case GET_PRODUCTS:
                getProducts("");
                break;
            case BUY_PRODUCTS:
                System.out.println("2");
                break;
            case SELL_PRODUCTS:
                System.out.println("3");
                break;
            case SHOW_TRANSACTION:
                System.out.println("4");
                break;
            case EXIT:
                System.out.println("5");
                break;
            default:
                System.out.println(BAD_CHOOICE);
        }
    }

    public static Menu convertStringToMenu(String option) {
        for (Menu menu : Menu.values()) {
            if (menu.getName().toUpperCase().equals(option.toUpperCase())) {
                return menu;
            }
        }
        return Menu.BAD;
    }

    public static void printMenuContent() {
        System.out.println(CHOOSE_OPTION);
        for (Menu menu : Menu.values()) {
            if (!menu.equals(Menu.BAD)) {
                System.out.println(menu.getName());
            }
        }
    }

}
