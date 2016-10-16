package daniel.biniek.Product;

import main.java.jms.MTJms;

import javax.jms.JMSException;
import javax.jws.WebService;

@WebService(endpointInterface="daniel.biniek.Product.ProductService")
public class ProductServiceImpl implements ProductService{

    MTJms mtJms = new MTJms();


    public String getProducts() throws JMSException {
        return mtJms.receiveMessage();
    }

    public void createOrder(String mse) {
        mtJms.sendMessages(mse);
    }
}
