package main.java.daniel.biniek.product;

import java.util.Random;

public class Generator {

    Random random =  new Random();

    private int HOW_MANY =  random.nextInt(4)+1;
    private float MIN = 25.0f;
    private float MAX = 500.0f;
    public Long generateIds() {
        return Long.valueOf(random.nextInt(5)+1);
    }

    public float generatePrice(){
        float price = random.nextFloat() * (MAX - MIN) + MIN;
        int aux = (int)(price*100);
        float result = (float) (aux/100d);
        return result;
    }

}
