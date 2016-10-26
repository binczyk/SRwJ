package daniel.biniek.Product;

import javax.jms.JMSException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ProductService {

    @WebMethod(operationName = "getProducts")
    public String[] getProducts() throws JMSException;

    @WebMethod(operationName = "createOrder")
    public void createOrder(@WebParam(name="method")String mse, @WebParam(name="productName")String productName,
                            @WebParam(name="destination")String destination, @WebParam(name="price")String price, @WebParam(name="amount")String amount);
}
