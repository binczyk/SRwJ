package main.java.daniel.biniek.product;

import java.io.Serializable;

public enum Product implements Serializable{
    GOLD(1l,"gold"),
    SILVER(2l,"silver"),
    PALLADIUM(3l,"palladium"),
    T_BILL(4l,"t-bill"),
    T_BOND(5l,"tbond");

    private String name;
    private Long id;

    private Product(Long id,String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
