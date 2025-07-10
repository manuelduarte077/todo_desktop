package dev.donmanuel.cartoons.ui.simpsons;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import dev.donmanuel.cartoons.core.database.AppDatabase;
import dev.donmanuel.cartoons.core.repository.FavoriteRepository;
import dev.donmanuel.cartoons.core.repository.SimpsonsRepository;
import dev.donmanuel.cartoons.model.Favorite;
import dev.donmanuel.cartoons.model.Simpsons;

public class SimpsonsViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;
    private final SimpsonsRepository simpsonsRepository;
    private final FavoriteRepository favoriteRepository;
    private final LiveData<List<Simpsons>> simpsonsData;
    private final LiveData<List<Integer>> favoriteSimpsonIds;
    
    // For search functionality
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MediatorLiveData<List<Simpsons>> filteredSimpsonsData = new MediatorLiveData<>();

    public SimpsonsViewModel(@NonNull Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("Simpsons");

        simpsonsRepository = new SimpsonsRepository();
        simpsonsData = simpsonsRepository.getSimpsonsData();

        AppDatabase database = AppDatabase.getDatabase(application);
        favoriteRepository = new FavoriteRepository(database.favoriteDao());

        // Transform favorites list to a list of just the IDs for easier checking
        favoriteSimpsonIds = Transformations.map(
            favoriteRepository.getSimpsonsFavorites(),
            favorites -> {
                List<Integer> ids = new ArrayList<>();
                for (Favorite favorite : favorites) {
                    ids.add(favorite.getItemId());
                }
                return ids;
            }
        );
        
        // Setup filtered data based on search query
        setupFilteredData();
    }

    private void setupFilteredData() {
        // Add source for original data
        filteredSimpsonsData.addSource(simpsonsData, episodes -> {
            filterEpisodes(episodes, searchQuery.getValue());
        });
        
        // Add source for search query changes
        filteredSimpsonsData.addSource(searchQuery, query -> {
            filterEpisodes(simpsonsData.getValue(), query);
        });
    }
    
    private void filterEpisodes(List<Simpsons> episodes, String query) {
        if (episodes == null) {
            filteredSimpsonsData.setValue(null);
            return;
        }
        
        if (query == null || query.isEmpty()) {
            filteredSimpsonsData.setValue(episodes);
            return;
        }
        
        String lowerCaseQuery = query.toLowerCase().trim();
        List<Simpsons> filteredList = new ArrayList<>();
        
        for (Simpsons episode : episodes) {
            // Search in name and description
            if ((episode.getName() != null && episode.getName().toLowerCase().contains(lowerCaseQuery)) ||
                (episode.getDescription() != null && episode.getDescription().toLowerCase().contains(lowerCaseQuery))) {
                filteredList.add(episode);
            }
        }
        
        filteredSimpsonsData.setValue(filteredList);
    }
    
    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Simpsons>> getSimpsonsData() {
        return filteredSimpsonsData;
    }

    public LiveData<List<Integer>> getFavoriteSimpsonIds() {
        return favoriteSimpsonIds;
    }

    public void toggleFavorite(Simpsons simpsons, boolean isFavorite) {
        if (isFavorite) {
            favoriteRepository.addToFavorites(simpsons);
        } else {
            favoriteRepository.removeFromFavorites(simpsons);
        }
    }
}
