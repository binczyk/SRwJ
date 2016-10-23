package daniel.biniek.Controller;

import daniel.biniek.Request.SOAPClient;
import daniel.biniek.product.ProductOb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductController {

    private static String CHOOSE_OPTION = "Choose option:";
    private static String BAD_CHOOICE = "It's a bad choice. Try these:";

    private static List<ProductOb> getProducts(String soapAction) throws Exception {
        return SOAPClient.get();
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

    private static enum METHOD{
        Buy,
        Sell,
        get
    }

    public static void chooseAction(Menu menu) throws Exception {
        Scanner in = new Scanner(System.in);
        String product;
        List<ProductOb> productObs = getProducts(METHOD.get.name());
        switch (menu) {
            case GET_PRODUCTS:
                productObs.forEach(pr -> System.out.println(pr.getName() + " " + pr.getPrice()));
                System.out.println();
                break;
            case BUY_PRODUCTS:
                product = in.nextLine();
                creatOrder(METHOD.Buy.name(), getBestProductByNameAndMethod(product, METHOD.Buy.name(), productObs));
                break;
            case SELL_PRODUCTS:
                product = in.nextLine();
                creatOrder(METHOD.Sell.name(), getBestProductByNameAndMethod(product, METHOD.Sell.name(), productObs));
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

    private static ProductOb getBestProductByNameAndMethod(String product, String method, List<ProductOb> productObs) {

        if(method.equals(METHOD.Buy.name())){
            return getMin(productObs.stream().filter(productOb -> productOb.getName().equals(product)).collect(Collectors.toList()));
        }else if(method.equals(METHOD.Sell.name())){
            return getMax(productObs.stream().filter(productOb -> productOb.getName().equals(product)).collect(Collectors.toList()));
        }

        return new ProductOb();

    }

    private static ProductOb getMin(List<ProductOb> collect) {

        ProductOb smallest = collect.get(0);
        for(ProductOb productOb : collect){
            if(productOb.getPrice() < smallest.getPrice()){
                smallest = productOb;
            }
        }
        return smallest;
    }

    private static ProductOb getMax(List<ProductOb> collect) {
        ProductOb smallest = collect.get(0);
        for(ProductOb productOb : collect){
            if(productOb.getPrice() > smallest.getPrice()){
                smallest = productOb;
            }
        }
        return smallest;
    }


    private static void creatOrder(String type, ProductOb product) throws Exception  {
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
