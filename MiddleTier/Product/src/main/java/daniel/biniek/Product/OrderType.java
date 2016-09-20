package daniel.biniek.Product;

public enum OrderType {
    BUY("buy"),
    SELL("sell");

    private String name;

    private OrderType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
