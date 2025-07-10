package dev.donmanuel.rickandmorty.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import dev.donmanuel.rickandmorty.R;
import dev.donmanuel.rickandmorty.database.Favorite;

public class FavoritesFragment extends Fragment implements FavoritesAdapter.OnFavoriteClickListener {

    private FavoritesViewModel viewModel;
    private FavoritesAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ChipGroup filterChipGroup;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        emptyView = view.findViewById(R.id.emptyFavoritesView);
        filterChipGroup = view.findViewById(R.id.filterChipGroup);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new FavoritesAdapter(this);
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        // Set up chip filters
        setupFilterChips();

        // Observe current filter
        viewModel.getCurrentFilter().observe(getViewLifecycleOwner(), filter -> {
            updateFilterChips(filter);
            updateFavoritesList(filter);
        });
    }

    private void setupFilterChips() {
        Chip allChip = filterChipGroup.findViewById(R.id.chipAll);
        Chip charactersChip = filterChipGroup.findViewById(R.id.chipCharacters);
        Chip locationsChip = filterChipGroup.findViewById(R.id.chipLocations);
        Chip episodesChip = filterChipGroup.findViewById(R.id.chipEpisodes);

        allChip.setOnClickListener(v -> viewModel.setFilter("all"));
        charactersChip.setOnClickListener(v -> viewModel.setFilter("character"));
        locationsChip.setOnClickListener(v -> viewModel.setFilter("location"));
        episodesChip.setOnClickListener(v -> viewModel.setFilter("episode"));
    }

    private void updateFilterChips(String filter) {
        Chip allChip = filterChipGroup.findViewById(R.id.chipAll);
        Chip charactersChip = filterChipGroup.findViewById(R.id.chipCharacters);
        Chip locationsChip = filterChipGroup.findViewById(R.id.chipLocations);
        Chip episodesChip = filterChipGroup.findViewById(R.id.chipEpisodes);

        allChip.setChecked(filter.equals("all"));
        charactersChip.setChecked(filter.equals("character"));
        locationsChip.setChecked(filter.equals("location"));
        episodesChip.setChecked(filter.equals("episode"));
    }

    private void updateFavoritesList(String filter) {
        switch (filter) {
            case "character":
                viewModel.getCharacterFavorites().observe(getViewLifecycleOwner(), favorites -> {
                    adapter.setFavorites(favorites);
                    updateEmptyView(favorites.isEmpty());
                });
                break;
            case "location":
                viewModel.getLocationFavorites().observe(getViewLifecycleOwner(), favorites -> {
                    adapter.setFavorites(favorites);
                    updateEmptyView(favorites.isEmpty());
                });
                break;
            case "episode":
                viewModel.getEpisodeFavorites().observe(getViewLifecycleOwner(), favorites -> {
                    adapter.setFavorites(favorites);
                    updateEmptyView(favorites.isEmpty());
                });
                break;
            default:
                viewModel.getAllFavorites().observe(getViewLifecycleOwner(), favorites -> {
                    adapter.setFavorites(favorites);
                    updateEmptyView(favorites.isEmpty());
                });
                break;
        }
    }

    private void updateEmptyView(boolean isEmpty) {
        if (isEmpty) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFavoriteClick(Favorite favorite) {
        // Handle favorite click (could navigate to detail screen)
    }

    @Override
    public void onRemoveFavorite(Favorite favorite) {
        viewModel.removeFromFavorites(favorite.getItemId());
    }
}
