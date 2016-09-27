package daniel.biniek.Controller;


public enum Menu {
    GET_PRODUCTS("Get products"),
    SELL_PRODUCTS("Sell product"),
    BUY_PRODUCTS("Buy product"),
    SHOW_TRANSACTION("Show transaction"),
    EXIT("Exit"),
    BAD("Bad");

    private String name;

    private Menu(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
