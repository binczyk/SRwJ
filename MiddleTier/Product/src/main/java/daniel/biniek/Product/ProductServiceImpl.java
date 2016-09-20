package daniel.biniek.Product;

import javax.jws.WebService;
import javax.xml.ws.Response;
import java.util.Arrays;
import java.util.List;

@WebService(endpointInterface="daniel.biniek.Product.ProductService")
public class ProductServiceImpl implements ProductService{

    public OrderType getProducts() {
        return OrderType.BUY;
    }

    public OrderType createOrder(OrderType type) {
        return OrderType.SELL;
    }
}
