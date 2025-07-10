package dev.donmanuel.fakeapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.donmanuel.fakeapi.adapters.ProductAdapter;
import dev.donmanuel.fakeapi.models.Product;
import dev.donmanuel.fakeapi.network.ApiCallback;
import dev.donmanuel.fakeapi.network.ApiClient;

/**
 * Main activity that displays a list of products
 * Following the Single Responsibility Principle
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupRecyclerView();
        fetchProducts();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(new ArrayList<>(), this::navigateToProductDetail);
        recyclerView.setAdapter(productAdapter);
    }

    private void fetchProducts() {
        ApiClient apiClient = ApiClient.getInstance();

        apiClient.fetchProducts(new ApiCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                runOnUiThread(() -> updateProductList(products));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> showError(e.getMessage()));
            }
        });
    }

    private void updateProductList(List<Product> products) {
        productAdapter.updateData(products);
    }

    private void navigateToProductDetail(Product product) {
        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
        intent.putExtra("productId", product.getId());
        startActivity(intent);
    }

    private void showError(String message) {
        Toast.makeText(MainActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}