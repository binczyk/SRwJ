package daniel.biniek.Product;

import daniel.biniek.product.ProductOb;

import javax.jms.JMSException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ProductService {

    @WebMethod(operationName = "getProducts")
    public ProductOb[] getProducts() throws JMSException;

    @WebMethod(operationName = "createOrder")
    public void createOrder(String mse, String productName);
}
