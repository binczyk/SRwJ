package daniel.biniek.product;

import java.io.Serializable;

public class ProductOb implements Serializable{

    String name;
    Long id;
    float priceBuy;
    float priceSell;
    String backName;

    public ProductOb() {
    }

    public ProductOb(String name, Long id, float priceBuy, float priceSell, String backName) {
        this.name = name;
        this.id = id;
        this.priceBuy = priceBuy;
        this.priceSell = priceSell;
        this.backName = backName;
    }

    public ProductOb(String name, Long id, float buy, float sell) {
        this.name = name;
        this.id = id;
        this.priceSell = sell;
        this.priceBuy = buy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getPriceBuy() {
        return priceBuy;
    }

    public void setPriceBuy(float priceBuy) {
        this.priceBuy = priceBuy;
    }

    public float getPriceSell() {
        return priceSell;
    }

    public void setPriceSell(float priceSell) {
        this.priceSell = priceSell;
    }

    public String getBackName() {
        return backName;
    }

    public void setBackName(String backName) {
        this.backName = backName;
    }

    @Override
    public String toString() {
        return "ProductOb{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", priceBuy=" + priceBuy +
                ", priceSell=" + priceSell +
                ", backName='" + backName + '\'' +
                '}';
    }
}
