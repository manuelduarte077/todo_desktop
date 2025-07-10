package dev.donmanuel.cartoons.ui.futurama;

import android.os.Bundle;
import android.text.TextUtils;
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

import dev.donmanuel.cartoons.databinding.FragmentFuturamaBinding;
import dev.donmanuel.cartoons.ui.futurama.adapter.FuturamaAdapter;

public class FuturamaFragment extends Fragment {

    private FragmentFuturamaBinding binding;
    private FuturamaViewModel futuramaViewModel;
    private FuturamaAdapter futuramaAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        futuramaViewModel =
                new ViewModelProvider(this).get(FuturamaViewModel.class);

        binding = FragmentFuturamaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textFuturama;
        futuramaViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        setupSearchView();
        setupRecyclerView();
        observeData();

        return root;
    }

    private void setupSearchView() {
        binding.searchViewFuturama.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                futuramaViewModel.setSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                futuramaViewModel.setSearchQuery(newText);
                return true;
            }
        });
    }

    private void setupRecyclerView() {
        futuramaAdapter = new FuturamaAdapter();

        // Set click listener for items
        futuramaAdapter.setOnItemClickListener(futurama -> {
            // Handle item click (e.g., show details)
        });

        // Set click listener for favorite button
        futuramaAdapter.setOnFavoriteClickListener((futurama, isFavorite) ->
                futuramaViewModel.toggleFavorite(futurama, isFavorite)
        );

        RecyclerView recyclerView = binding.recyclerViewFuturama;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(futuramaAdapter);
    }

    private void observeData() {
        // Observe Futurama data
        futuramaViewModel.getFuturamaData().observe(getViewLifecycleOwner(), futuramaList -> {
            if (futuramaList != null) {
                futuramaAdapter.setFuturamaList(futuramaList);
                updateLoadingState(false);

                // Show empty state message if no results
                if (futuramaList.isEmpty() && !TextUtils.isEmpty(binding.searchViewFuturama.getQuery())) {
                    binding.textNoResults.setVisibility(View.VISIBLE);
                } else {
                    binding.textNoResults.setVisibility(View.GONE);
                }
            }
        });

        // Observe favorite IDs to update the UI accordingly
        futuramaViewModel.getFavoriteFuturamaIds().observe(getViewLifecycleOwner(), favoriteIds -> {
            if (favoriteIds != null) {
                futuramaAdapter.setFavoriteIds(favoriteIds);
            }
        });
    }

    private void updateLoadingState(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.recyclerViewFuturama.setVisibility(View.GONE);
            binding.textNoResults.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.recyclerViewFuturama.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
