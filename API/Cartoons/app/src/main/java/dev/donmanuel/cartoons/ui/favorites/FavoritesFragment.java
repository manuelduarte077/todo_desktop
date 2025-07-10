package dev.donmanuel.cartoons.ui.favorites;

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

import dev.donmanuel.cartoons.R;
import dev.donmanuel.cartoons.databinding.FragmentFavoritesBinding;
import dev.donmanuel.cartoons.ui.favorites.adapter.FavoritesAdapter;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private FavoritesViewModel favoritesViewModel;
    private FavoritesAdapter favoritesAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up header text
        final TextView textView = binding.textFavorites;
        favoritesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Set up RecyclerView
        setupRecyclerView();

        // Set up filter chips
        setupFilterChips();

        // Observe favorites data based on current filter
        observeFavorites();

        return root;
    }

    private void setupRecyclerView() {
        favoritesAdapter = new FavoritesAdapter();
        favoritesAdapter.setOnDeleteClickListener(favorite ->
            favoritesViewModel.removeFavorite(favorite)
        );

        RecyclerView recyclerView = binding.recyclerViewFavorites;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(favoritesAdapter);
    }

    private void setupFilterChips() {
        ChipGroup chipGroup = binding.chipGroupFilter;

        // Set click listeners for filter chips
        binding.chipAll.setOnClickListener(v ->
            favoritesViewModel.setFilter(FavoritesViewModel.FILTER_ALL)
        );

        binding.chipSimpsons.setOnClickListener(v ->
            favoritesViewModel.setFilter(FavoritesViewModel.FILTER_SIMPSONS)
        );

        binding.chipFuturama.setOnClickListener(v ->
            favoritesViewModel.setFilter(FavoritesViewModel.FILTER_FUTURAMA)
        );

        // Update chip selection based on current filter
        favoritesViewModel.getCurrentFilter().observe(getViewLifecycleOwner(), filter -> {
            switch (filter) {
                case FavoritesViewModel.FILTER_SIMPSONS:
                    binding.chipSimpsons.setChecked(true);
                    break;
                case FavoritesViewModel.FILTER_FUTURAMA:
                    binding.chipFuturama.setChecked(true);
                    break;
                default:
                    binding.chipAll.setChecked(true);
                    break;
            }
        });
    }

    private void observeFavorites() {
        favoritesViewModel.getCurrentFilter().observe(getViewLifecycleOwner(), filter -> {
            switch (filter) {
                case FavoritesViewModel.FILTER_SIMPSONS:
                    favoritesViewModel.getSimpsonsFavorites().observe(
                            getViewLifecycleOwner(),
                            favorites -> {
                                favoritesAdapter.setFavorites(favorites);
                                updateEmptyView(favorites.isEmpty());
                            }
                    );
                    break;
                case FavoritesViewModel.FILTER_FUTURAMA:
                    favoritesViewModel.getFuturamaFavorites().observe(
                            getViewLifecycleOwner(),
                            favorites -> {
                                favoritesAdapter.setFavorites(favorites);
                                updateEmptyView(favorites.isEmpty());
                            }
                    );
                    break;
                default:
                    favoritesViewModel.getAllFavorites().observe(
                            getViewLifecycleOwner(),
                            favorites -> {
                                favoritesAdapter.setFavorites(favorites);
                                updateEmptyView(favorites.isEmpty());
                            }
                    );
                    break;
            }
        });
    }

    private void updateEmptyView(boolean isEmpty) {
        if (isEmpty) {
            binding.textEmptyFavorites.setVisibility(View.VISIBLE);
            binding.recyclerViewFavorites.setVisibility(View.GONE);
        } else {
            binding.textEmptyFavorites.setVisibility(View.GONE);
            binding.recyclerViewFavorites.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
