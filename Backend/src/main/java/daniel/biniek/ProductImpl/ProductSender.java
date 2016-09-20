package daniel.biniek.ProductImpl;


import daniel.biniek.Utils.Generator;

import java.util.*;
import java.util.stream.Collectors;

public class ProductSender {

    private Generator generator = new Generator();

    public List<Product> randomProducts(){
        Set<Long> ids = generator.generateIds();
        List<Product> products;
        products = ids.stream().map(id -> getProductById(id)).collect(Collectors.toList());

        return products;
    }

    private Product getProductById(Long id){
        for (Product product : Product.values()) {
            if(product.getId().equals(id)){
                product.setPrice(generator.generatePrice());
                return product;
            }
        }
        return null;
    }



}
