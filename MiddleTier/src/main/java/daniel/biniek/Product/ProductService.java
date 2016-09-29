package daniel.biniek.Product;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ProductService {

    @WebMethod(operationName = "getProducts")
    public String getProducts();

    @WebMethod(operationName = "createOrder")
    public OrderType createOrder(OrderType type);
}
