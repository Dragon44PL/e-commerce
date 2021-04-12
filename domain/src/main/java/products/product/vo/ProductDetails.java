package products.product.vo;

public record ProductDetails(String name, String description) {

    public ProductDetails changeName(String name) {
        return new ProductDetails(name, description);
    }

    public ProductDetails changeDescription(String description) {
        return new ProductDetails(name, description);
    }

}
