package daniel.biniek.Product;

import daniel.biniek.jms.MTJms;
import daniel.biniek.product.ProductOb;

import javax.jms.JMSException;
import javax.jws.WebService;
import java.util.ArrayList;

@WebService(endpointInterface="daniel.biniek.Product.ProductService")
public class ProductServiceImpl implements ProductService{

    private MTJms mtJms = new MTJms();

    @Override
    public ProductOb[] getProducts(){
        try {
            return prepareProductArray();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ProductOb[] prepareProductArray() throws JMSException {
        ArrayList<ProductOb> productList = (ArrayList<ProductOb>) mtJms.receiveMessage();
        ProductOb[] productObs = new ProductOb[productList.size()];
        productObs = productList.toArray(productObs);
        return productObs;
    }

    @Override
    public void createOrder(String mse, String productName) {
        mtJms.sendMessages(mse);
    }
}
