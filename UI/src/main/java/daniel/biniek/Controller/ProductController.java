package daniel.biniek.Controller;

import daniel.biniek.Request.SOAPClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductController {

    private static String CHOOSE_OPTION = "Choose option:";
    private static String BAD_CHOOICE = "It's a bad choice. Try these:";
    private static List<String> productObs = new ArrayList<>();

    private static ReadProduct readProduct = new ReadProduct();

    private static List<String> getProducts(String soapAction) throws Exception {
        return SOAPClient.get();
    }

    private static String getFileContent(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
            in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return sb.toString();
    }

    private enum METHOD {
        Buy,
        Sell,
        get
    }

    public static void chooseAction(Menu menu) throws Exception {
        Scanner in = new Scanner(System.in);
        String product;
        String price;
        String amount;
        switch (menu) {
            case GET_PRODUCTS:
                productObs = getProducts(METHOD.get.name());
                productObs.forEach(pr -> System.out.println(readProduct.read(pr)));
                System.out.println();
                break;
            case BUY_PRODUCTS:
                if (productObs.isEmpty()) {
                    System.out.println("Download list of products");
                } else {
                    System.out.println("Product name:");
                    product = in.nextLine();
                    System.out.println("Product price:");
                    price = in.nextLine();
                    System.out.println("Product amount:");
                    amount = in.nextLine();
                    try {
                        Double.parseDouble(price);
                        Long.parseLong(amount);
                        String pr = getProductByName(product);
                        if (!pr.isEmpty()) {
                            creatOrder(METHOD.Buy.name(), pr.concat(";").concat(price).concat(";").concat(amount));
                        } else {
                            System.out.println("Product does not exist!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Incorect value!");
                    }
                }
                break;
            case SELL_PRODUCTS:
                if (productObs.isEmpty()) {
                    System.out.println("Download list of products");
                } else {
                    System.out.println("Product name:");
                    product = in.nextLine();
                    System.out.println("Product price:");
                    price = in.nextLine();
                    System.out.println("Product amount:");
                    amount = in.nextLine();
                    try {
                        Double.parseDouble(price);
                        Long.parseLong(amount);
                        String pr = getProductByName(product);
                        if (!pr.isEmpty()) {
                            creatOrder(METHOD.Sell.name(), pr.concat(";").concat(price).concat(";").concat(amount));
                        } else {
                            System.out.println("Product does not exist!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Incorect value!");
                    }
                }
                break;
            /*case SHOW_TRANSACTION:
                System.out.println("4");
                break;*/
            case EXIT:
                System.out.println("5");
                break;
            default:
                System.out.println(BAD_CHOOICE);
        }
    }

    private static String getProductByName(String product) {
        for (String p : productObs) {
            if (readProduct.readName(p).equals(product)) {
                return p;
            }
        }
        return "";
    }


    private static void creatOrder(String type, String product) throws Exception {
        SOAPClient.create(type, product);
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
