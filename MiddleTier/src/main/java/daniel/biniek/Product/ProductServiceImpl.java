package daniel.biniek.Product;

import daniel.biniek.jms.MTJms;

import javax.jms.JMSException;
import javax.jws.WebService;

@WebService(endpointInterface="daniel.biniek.Product.ProductService")
public class ProductServiceImpl implements ProductService{

    private MTJms mtJms = new MTJms();

    public String getProducts(){
        try {
            return mtJms.receiveMessage();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return new String();
    }

    public void createOrder(String mse) {
        mtJms.sendMessages(mse);
    }
}
