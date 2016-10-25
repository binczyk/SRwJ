package daniel.biniek.Product;

import daniel.biniek.jms.MTJms;

import javax.jms.JMSException;
import javax.jws.WebService;
import java.util.ArrayList;

@WebService(endpointInterface="daniel.biniek.Product.ProductService")
public class ProductServiceImpl implements ProductService{

    private MTJms mtJms = new MTJms();

    @Override
    public String[] getProducts(){
        try {
            return prepareProductArray();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[] prepareProductArray() throws JMSException {
        ArrayList<String> productList = (ArrayList<String>) mtJms.receiveMessage();
        String[] productObs = new String[productList.size()];
        productObs = productList.toArray(productObs);
        return productObs;
    }

    @Override
    public void createOrder(String mse, String productName, String destination) {
        mtJms.sendMessages(mse, productName, destination);
    }
}
