package daniel.biniek.Product;

import javax.jws.WebService;

@WebService(endpointInterface="daniel.biniek.Product.ProductService")
public class ProductServiceImpl implements ProductService{

    public String getProducts() {
        return "GOLD\nSILVER\nPLATINUM";
    }

    public OrderType createOrder(OrderType type) {
        return OrderType.SELL;
    }
}
