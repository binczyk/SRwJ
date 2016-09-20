package daniel.biniek.Utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Generator {

    Random random =  new Random();

    private int HOW_MANY =  random.nextInt(4)+1;
    private float MIN = 25.0f;
    private float MAX = 500.0f;
    public Set<Long> generateIds() {
        Set<Long> ids = new HashSet<Long>();
        for(int i=0; i<HOW_MANY;i++){
            ids.add(Long.valueOf(random.nextInt(4)+1));
        }
        return ids;
    }

    public float generatePrice(){
        float price = random.nextFloat() * (MAX - MIN) + MIN;
        int aux = (int)(price*100);
        float result = (float) (aux/100d);
        return result;
    }

}
