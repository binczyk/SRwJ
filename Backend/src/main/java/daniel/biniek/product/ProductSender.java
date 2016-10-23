package daniel.biniek.product;


public class ProductSender {

    private Generator generator = new Generator();

    public ProductOb randomProduct() {
        Product product = getProductById(generator.generateIds());
        ProductOb productOb = new ProductOb(product.getName(), product.getId(), generator.generatePrice(), generator.generatePrice());
        return productOb;
    }

    private Product getProductById(Long id) {
        for (Product product : Product.values()) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }


}
