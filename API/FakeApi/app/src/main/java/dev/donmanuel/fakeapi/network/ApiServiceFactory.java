package dev.donmanuel.fakeapi.network;

import com.google.gson.Gson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;

/**
 * Factory for creating API service instances
 * Following the Dependency Inversion Principle
 */
public class ApiServiceFactory {
    
    private static final int DEFAULT_THREAD_POOL_SIZE = 4;
    
    public static ApiService createDefaultApiService() {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        
        return new OkHttpApiService(client, gson, executorService);
    }
    
    // Allows for custom configuration if needed
    public static ApiService createCustomApiService(OkHttpClient client, Gson gson, ExecutorService executorService) {
        return new OkHttpApiService(client, gson, executorService);
    }
}
