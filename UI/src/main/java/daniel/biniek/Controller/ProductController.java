package daniel.biniek.Controller;

import daniel.biniek.Request.SOAPClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
                    String best = getBestProductByNameAndMethod(product, METHOD.Buy.name(), productObs);
                    creatOrder(METHOD.Buy.name(), best);
                }
                break;
            case SELL_PRODUCTS:
                if (productObs.isEmpty()) {
                    System.out.println("Download list of products");
                } else {
                    System.out.println("Product name:");
                    product = in.nextLine();
                    String bestSell = getBestProductByNameAndMethod(product, METHOD.Sell.name(), productObs);
                    creatOrder(METHOD.Sell.name(), bestSell);
                }
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

    private static String getBestProductByNameAndMethod(String product, String method, List<String> productObs) {

        if (method.equals(METHOD.Buy.name())) {
            return getMin(productObs.stream().filter(productOb -> readProduct.readName(productOb).equals(product)).collect(Collectors.toList()));
        } else if (method.equals(METHOD.Sell.name())) {
            return getMax(productObs.stream().filter(productOb -> readProduct.readName(productOb).equals(product)).collect(Collectors.toList()));
        }

        return new String();

    }

    private static String getMin(List<String> collect) {

        String smallest = collect.get(0);
        for (String productOb : collect) {
            if (Double.parseDouble(readProduct.readBuy(productOb)) < Double.parseDouble(readProduct.readBuy(smallest))) {
                smallest = productOb;
            }
        }
        return smallest;
    }

    private static String getMax(List<String> collect) {

        String largest = collect.get(0);
        for (String productOb : collect) {
            if (Double.parseDouble(readProduct.readSell(productOb)) > Double.parseDouble(readProduct.readSell(largest))) {
                largest = productOb;
            }
        }
        return largest;
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
