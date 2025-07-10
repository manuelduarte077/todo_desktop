package dev.donmanuel.fakeapi.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutorService;

import dev.donmanuel.fakeapi.models.Product;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Implementation of ApiService using OkHttp
 * Following the Single Responsibility Principle
 */
public class OkHttpApiService implements ApiService {
    private static final String TAG = OkHttpApiService.class.getSimpleName();
    private static final String BASE_URL = "https://api.escuelajs.co/api/v1";
    
    private final OkHttpClient client;
    private final Gson gson;
    private final ExecutorService executorService;

    public OkHttpApiService(OkHttpClient client, Gson gson, ExecutorService executorService) {
        this.client = client;
        this.gson = gson;
        this.executorService = executorService;
    }

    @Override
    public void fetchProducts(ApiCallback<List<Product>> callback) {
        String url = BASE_URL + "/products";
        executeRequest(url, new TypeToken<List<Product>>() {}.getType(), callback);
    }

    @Override
    public void fetchProductById(int productId, ApiCallback<Product> callback) {
        String url = BASE_URL + "/products/" + productId;
        executeRequest(url, Product.class, callback);
    }

    private <T> void executeRequest(String url, Type type, ApiCallback<T> callback) {
        Request request = new Request.Builder().url(url).build();

        executorService.execute(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    T result = gson.fromJson(responseBody, type);
                    callback.onSuccess(result);
                } else {
                    String errorMessage = "Error: " + response.code();
                    Log.e(TAG, errorMessage);
                    callback.onError(new Exception(errorMessage));
                }
            } catch (Exception e) {
                Log.e(TAG, "Request failed", e);
                callback.onError(e);
            }
        });
    }
}
