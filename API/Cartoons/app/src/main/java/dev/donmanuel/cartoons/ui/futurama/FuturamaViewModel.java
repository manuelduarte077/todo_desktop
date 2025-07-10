package dev.donmanuel.cartoons.ui.futurama;

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
import dev.donmanuel.cartoons.core.repository.FuturamaRepository;
import dev.donmanuel.cartoons.model.Favorite;
import dev.donmanuel.cartoons.model.Futurama;

public class FuturamaViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;
    private final FuturamaRepository futuramaRepository;
    private final FavoriteRepository favoriteRepository;
    private final LiveData<List<Futurama>> futuramaData;
    private final LiveData<List<Integer>> favoriteFuturamaIds;
    
    // For search functionality
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MediatorLiveData<List<Futurama>> filteredFuturamaData = new MediatorLiveData<>();

    public FuturamaViewModel(@NonNull Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("Futurama");

        futuramaRepository = new FuturamaRepository();
        futuramaData = futuramaRepository.getFuturamaData();

        AppDatabase database = AppDatabase.getDatabase(application);
        favoriteRepository = new FavoriteRepository(database.favoriteDao());

        // Transform favorites list to a list of just the IDs for easier checking
        favoriteFuturamaIds = Transformations.map(
            favoriteRepository.getFuturamaFavorites(),
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
        filteredFuturamaData.addSource(futuramaData, episodes -> {
            filterEpisodes(episodes, searchQuery.getValue());
        });
        
        // Add source for search query changes
        filteredFuturamaData.addSource(searchQuery, query -> {
            filterEpisodes(futuramaData.getValue(), query);
        });
    }
    
    private void filterEpisodes(List<Futurama> episodes, String query) {
        if (episodes == null) {
            filteredFuturamaData.setValue(null);
            return;
        }
        
        if (query == null || query.isEmpty()) {
            filteredFuturamaData.setValue(episodes);
            return;
        }
        
        String lowerCaseQuery = query.toLowerCase().trim();
        List<Futurama> filteredList = new ArrayList<>();
        
        for (Futurama episode : episodes) {
            // Search in title, description, and slogan
            if ((episode.getTitle() != null && episode.getTitle().toLowerCase().contains(lowerCaseQuery)) ||
                (episode.getDescription() != null && episode.getDescription().toLowerCase().contains(lowerCaseQuery)) ||
                (episode.getSlogan() != null && episode.getSlogan().toLowerCase().contains(lowerCaseQuery)) ||
                (episode.getCategory() != null && episode.getCategory().toLowerCase().contains(lowerCaseQuery))) {
                filteredList.add(episode);
            }
        }
        
        filteredFuturamaData.setValue(filteredList);
    }
    
    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Futurama>> getFuturamaData() {
        return filteredFuturamaData;
    }

    public LiveData<List<Integer>> getFavoriteFuturamaIds() {
        return favoriteFuturamaIds;
    }

    public void toggleFavorite(Futurama futurama, boolean isFavorite) {
        if (isFavorite) {
            favoriteRepository.addToFavorites(futurama);
        } else {
            favoriteRepository.removeFromFavorites(futurama);
        }
    }
}
