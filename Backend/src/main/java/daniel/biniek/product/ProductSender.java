package main.java.daniel.biniek.product;



public class ProductSender {

    private Generator generator = new Generator();

    public ProductOb randomProduct(){
        Product product = getProductById(generator.generateIds());
        ProductOb productOb = new ProductOb(product.getName(),product.getId(),product.getPrice());
        return productOb;
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
