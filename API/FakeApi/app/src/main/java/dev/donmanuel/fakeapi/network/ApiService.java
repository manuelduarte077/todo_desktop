package dev.donmanuel.fakeapi.network;

import java.util.List;

import dev.donmanuel.fakeapi.models.Product;

/**
 * Interface defining the API operations
 * Following the Interface Segregation Principle
 */
public interface ApiService {
    void fetchProducts(ApiCallback<List<Product>> callback);
    void fetchProductById(int productId, ApiCallback<Product> callback);
}
