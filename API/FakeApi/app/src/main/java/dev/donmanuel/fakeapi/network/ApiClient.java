package dev.donmanuel.fakeapi.network;

import dev.donmanuel.fakeapi.models.Product;

import java.util.List;

/**
 * ApiClient facade that provides a simple interface to the API
 * Following the Single Responsibility Principle and Dependency Inversion Principle
 */
public class ApiClient {
    private static ApiClient instance;
    private final ApiService apiService;

    private ApiClient() {
        this.apiService = ApiServiceFactory.createDefaultApiService();
    }

    // For testing or custom configuration
    ApiClient(ApiService apiService) {
        this.apiService = apiService;
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public void fetchProducts(ApiCallback<List<Product>> callback) {
        apiService.fetchProducts(callback);
    }

    public void fetchProductById(int productId, ApiCallback<Product> callback) {
        apiService.fetchProductById(productId, callback);
    }
}