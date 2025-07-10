package dev.donmanuel.cartoons.ui.simpsons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.donmanuel.cartoons.databinding.FragmentSimpsonsBinding;
import dev.donmanuel.cartoons.ui.simpsons.adapter.SimpsonsAdapter;

public class SimpsonsFragment extends Fragment {

    private FragmentSimpsonsBinding binding;
    private SimpsonsViewModel simpsonsViewModel;
    private SimpsonsAdapter simpsonsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        simpsonsViewModel =
                new ViewModelProvider(this).get(SimpsonsViewModel.class);

        binding = FragmentSimpsonsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSimpsons;
        simpsonsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        setupSearchView();
        setupRecyclerView();
        observeData();

        return root;
    }

    private void setupSearchView() {
        binding.searchViewSimpsons.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                simpsonsViewModel.setSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                simpsonsViewModel.setSearchQuery(newText);
                return true;
            }
        });
    }

    private void setupRecyclerView() {
        simpsonsAdapter = new SimpsonsAdapter();

        // Set click listener for items
        simpsonsAdapter.setOnItemClickListener(simpsons -> {
            // Handle item click (e.g., show details)
        });

        // Set click listener for favorite button
        simpsonsAdapter.setOnFavoriteClickListener((simpsons, isFavorite) ->
                simpsonsViewModel.toggleFavorite(simpsons, isFavorite)
        );

        RecyclerView recyclerView = binding.recyclerViewSimpsons;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(simpsonsAdapter);
    }

    private void observeData() {
        // Observe Simpsons data
        simpsonsViewModel.getSimpsonsData().observe(getViewLifecycleOwner(), simpsonsList -> {
            if (simpsonsList != null) {
                simpsonsAdapter.setSimpsonsList(simpsonsList);
                updateLoadingState(false);

                // Show empty state message if no results
                if (simpsonsList.isEmpty() && !android.text.TextUtils.isEmpty(binding.searchViewSimpsons.getQuery())) {
                    binding.textNoResults.setVisibility(View.VISIBLE);
                } else {
                    binding.textNoResults.setVisibility(View.GONE);
                }
            }
        });

        // Observe favorite IDs to update the UI accordingly
        simpsonsViewModel.getFavoriteSimpsonIds().observe(getViewLifecycleOwner(), favoriteIds -> {
            if (favoriteIds != null) {
                simpsonsAdapter.setFavoriteIds(favoriteIds);
            }
        });
    }

    private void updateLoadingState(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.recyclerViewSimpsons.setVisibility(View.GONE);
            binding.textNoResults.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.recyclerViewSimpsons.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
