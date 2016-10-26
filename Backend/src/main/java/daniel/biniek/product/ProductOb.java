package daniel.biniek.product;

import java.io.Serializable;

public class ProductOb implements Serializable{

    String name;
    Long id;
    float price;
    String backName;
    Long amount;

    public ProductOb(String name, Long id) {
        this.name = name;
        this.id = id;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getBackName() {
        return backName;
    }

    public void setBackName(String backName) {
        this.backName = backName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ProductOb{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", price=" + price +
                ", backName='" + backName + '\'' +
                ", amount=" + amount +
                '}';
    }
}
