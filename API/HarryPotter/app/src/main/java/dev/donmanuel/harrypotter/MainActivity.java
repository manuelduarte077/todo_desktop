package dev.donmanuel.harrypotter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.donmanuel.harrypotter.presentation.adapter.CharacterAdapter;
import dev.donmanuel.harrypotter.presentation.view.CharacterDetailActivity;
import dev.donmanuel.harrypotter.presentation.viewmodel.CharacterViewModel;
import dev.donmanuel.harrypotter.presentation.viewmodel.CharacterViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private CharacterViewModel viewModel;
    private CharacterAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Initialize UI components
        setupUI();
        
        // Initialize ViewModel with factory
        CharacterViewModelFactory factory = new CharacterViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(CharacterViewModel.class);
        
        // Setup RecyclerView and adapter
        setupRecyclerView();
        
        // Observe LiveData from ViewModel
        observeViewModel();
        
        // Load characters
        viewModel.loadAllCharacters();
    }
    
    private void setupUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        recyclerView = findViewById(R.id.recycler_characters);
        progressBar = findViewById(R.id.progress_bar);
        textError = findViewById(R.id.text_error);
    }
    
    private void setupRecyclerView() {
        adapter = new CharacterAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        // Set click listener for character items
        adapter.setOnItemClickListener(character -> {
            // Navigate to detail screen
            Intent intent = new Intent(MainActivity.this, CharacterDetailActivity.class);
            intent.putExtra(CharacterDetailActivity.EXTRA_CHARACTER, character);
            startActivity(intent);
        });
    }
    
    private void observeViewModel() {
        // Observe characters data
        viewModel.getCharacters().observe(this, characters -> {
            if (characters != null && !characters.isEmpty()) {
                adapter.setCharacters(characters);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        
        // Observe loading state
        viewModel.getLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading) {
                textError.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });
        
        // Observe error state
        viewModel.getError().observe(this, errorMessage -> {
            textError.setText(errorMessage);
            textError.setVisibility(errorMessage != null ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(errorMessage != null ? View.GONE : View.VISIBLE);
        });
    }
}