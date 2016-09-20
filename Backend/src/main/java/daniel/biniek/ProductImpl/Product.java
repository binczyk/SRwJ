package daniel.biniek.ProductImpl;

public enum Product {
    GOLD(1l,"gold",0f),
    SILVER(2l,"silver",0f),
    PALLADIUM(3l,"palladium",0f),
    T_BILL(4l,"t-bill",0f),
    T_BOND(5l,"tbond",0f);

    private String name;
    private Long id;
    private float price;

    private Product(Long id,String name, float price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
