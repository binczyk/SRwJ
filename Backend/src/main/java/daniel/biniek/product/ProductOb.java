package daniel.biniek.product;

import java.io.Serializable;

public class ProductOb implements Serializable{

    String name;
    Long id;
    Double price;
    String backName;
    Long amount;

    public ProductOb() {
    }

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductOb productOb = (ProductOb) o;

        if (name != null ? !name.equals(productOb.name) : productOb.name != null) return false;
        if (id != null ? !id.equals(productOb.id) : productOb.id != null) return false;
        if (price != null ? !price.equals(productOb.price) : productOb.price != null) return false;
        if (backName != null ? !backName.equals(productOb.backName) : productOb.backName != null) return false;
        return amount != null ? amount.equals(productOb.amount) : productOb.amount == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (backName != null ? backName.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
