package dev.donmanuel.fakeapi;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dev.donmanuel.fakeapi.adapters.ImageCarouselAdapter;
import dev.donmanuel.fakeapi.models.Image;
import dev.donmanuel.fakeapi.models.Product;
import dev.donmanuel.fakeapi.network.ApiCallback;
import dev.donmanuel.fakeapi.network.ApiClient;

import com.google.android.material.appbar.MaterialToolbar;

/**
 * Activity for displaying product details
 * Following the Single Responsibility Principle
 */
public class ProductDetailActivity extends AppCompatActivity {

    private ImageView image;
    private TextView title, price, description;
    private RecyclerView imageRecyclerView;
    private ImageCarouselAdapter carouselAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupRecyclerView();

        int productId = getIntent().getIntExtra("productId", 0);
        if (productId == 0) {
            Toast.makeText(this, "Producto no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchProductDetails(productId);
    }

    private void initViews() {
        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        imageRecyclerView = findViewById(R.id.imageRecyclerView);
    }

    private void setupRecyclerView() {
        // Configurar el RecyclerView para el carrusel
        imageRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        // Crear adaptador vacío inicialmente
        carouselAdapter = new ImageCarouselAdapter(new ArrayList<>());
        imageRecyclerView.setAdapter(carouselAdapter);

        // Agregar PagerSnapHelper para efecto de paginación
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(imageRecyclerView);
    }

    private void fetchProductDetails(int productId) {
        ApiClient apiClient = ApiClient.getInstance();

        apiClient.fetchProductById(productId, new ApiCallback<Product>() {
            @Override
            public void onSuccess(Product product) {
                runOnUiThread(() -> updateUI(product));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> showError(e.getMessage()));
            }
        });
    }

    private void updateUI(Product product) {
        title.setText(product.getTitle());
        price.setText("$" + product.getPrice());
        description.setText(product.getDescription());
        
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            Picasso.get().load(product.getImages().get(0)).into(image);
            
            // Convertir las URLs de imágenes a objetos Image
            List<Image> images = new ArrayList<>();
            for (int i = 0; i < product.getImages().size(); i++) {
                images.add(new Image(i, product.getImages().get(i)));
            }
            
            // Actualizar el adaptador del carrusel de imágenes
            carouselAdapter.updateData(images);
        }
    }

    private void showError(String message) {
        Toast.makeText(ProductDetailActivity.this,
                "Error al cargar detalles: " + message,
                Toast.LENGTH_SHORT).show();
    }
}