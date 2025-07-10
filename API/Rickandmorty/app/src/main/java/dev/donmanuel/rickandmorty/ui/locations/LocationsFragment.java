package dev.donmanuel.rickandmorty.ui.locations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.donmanuel.rickandmorty.R;
import dev.donmanuel.rickandmorty.model.Location;
import dev.donmanuel.rickandmorty.ui.favorites.FavoritesViewModel;

public class LocationsFragment extends Fragment implements LocationsAdapter.OnLocationClickListener {

    private LocationsViewModel viewModel;
    private FavoritesViewModel favoritesViewModel;
    private LocationsAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar loadingProgressBar;
    private LinearLayout errorLayout;
    private Button retryButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_locations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerView = view.findViewById(R.id.locationsRecyclerView);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        errorLayout = view.findViewById(R.id.errorLayout);
        retryButton = view.findViewById(R.id.retryButton);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new LocationsAdapter(this);
        recyclerView.setAdapter(adapter);

        // Set up scroll listener for pagination
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!viewModel.getIsLoading().getValue() &&
                            (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        // Load more locations
                        viewModel.loadLocations();
                    }
                }
            }
        });

        // Set up retry button
        retryButton.setOnClickListener(v -> viewModel.refreshLocations());

        // Initialize ViewModels
        viewModel = new ViewModelProvider(this).get(LocationsViewModel.class);
        favoritesViewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);

        // Observe ViewModel data
        viewModel.getLocations().observe(getViewLifecycleOwner(), locations -> {
            if (locations != null && !locations.isEmpty()) {
                adapter.setLocations(locations);
                showContent();
                
                // Check favorite status for each location
                for (Location location : locations) {
                    String locationId = String.valueOf(location.getId());
                    boolean isFavorite = favoritesViewModel.isFavorite(locationId);
                    if (isFavorite) {
                        adapter.updateFavoriteStatus(locationId, true);
                    }
                }
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                if (adapter.getItemCount() == 0) {
                    showLoading();
                } else {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                }
            } else {
                loadingProgressBar.setVisibility(View.GONE);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                if (adapter.getItemCount() == 0) {
                    showError();
                } else {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showContent() {
        recyclerView.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }

    private void showLoading() {
        recyclerView.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }

    private void showError() {
        recyclerView.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLocationClick(Location location) {
        Toast.makeText(requireContext(), location.getName(), Toast.LENGTH_SHORT).show();
        // You could navigate to a detail screen here
    }

    @Override
    public void onFavoriteClick(Location location, boolean isFavorite) {
        if (isFavorite) {
            favoritesViewModel.addLocationToFavorites(location);
            Toast.makeText(requireContext(), location.getName() + " added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            favoritesViewModel.removeFromFavorites(String.valueOf(location.getId()));
            Toast.makeText(requireContext(), location.getName() + " removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }
}
