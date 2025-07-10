package dev.donmanuel.rickandmorty.ui.favorites;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import dev.donmanuel.rickandmorty.database.Favorite;
import dev.donmanuel.rickandmorty.database.FavoriteRepository;
import dev.donmanuel.rickandmorty.model.Character;
import dev.donmanuel.rickandmorty.model.Episode;
import dev.donmanuel.rickandmorty.model.Location;

public class FavoritesViewModel extends AndroidViewModel {

    private final FavoriteRepository repository;
    private final LiveData<List<Favorite>> allFavorites;
    private final LiveData<List<Favorite>> characterFavorites;
    private final LiveData<List<Favorite>> locationFavorites;
    private final LiveData<List<Favorite>> episodeFavorites;
    private final MutableLiveData<String> currentFilter = new MutableLiveData<>("all");

    public FavoritesViewModel(Application application) {
        super(application);
        repository = new FavoriteRepository(application);
        allFavorites = repository.getAllFavorites();
        characterFavorites = repository.getCharacterFavorites();
        locationFavorites = repository.getLocationFavorites();
        episodeFavorites = repository.getEpisodeFavorites();
    }

    public LiveData<List<Favorite>> getAllFavorites() {
        return allFavorites;
    }

    public LiveData<List<Favorite>> getCharacterFavorites() {
        return characterFavorites;
    }

    public LiveData<List<Favorite>> getLocationFavorites() {
        return locationFavorites;
    }

    public LiveData<List<Favorite>> getEpisodeFavorites() {
        return episodeFavorites;
    }

    public LiveData<String> getCurrentFilter() {
        return currentFilter;
    }

    public void setFilter(String filter) {
        currentFilter.setValue(filter);
    }

    public void addCharacterToFavorites(Character character) {
        Favorite favorite = new Favorite(
                String.valueOf(character.getId()),
                character.getName(),
                character.getImage(),
                "character",
                "Status: " + character.getStatus() + ", Species: " + character.getSpecies()
        );
        repository.insert(favorite);
    }

    public void addLocationToFavorites(Location location) {
        Favorite favorite = new Favorite(
                String.valueOf(location.getId()),
                location.getName(),
                "",  // Locations don't have images
                "location",
                "Type: " + location.getType() + ", Dimension: " + location.getDimension()
        );
        repository.insert(favorite);
    }

    public void addEpisodeToFavorites(Episode episode) {
        Favorite favorite = new Favorite(
                String.valueOf(episode.getId()),
                episode.getName(),
                "",  // Episodes don't have images
                "episode",
                "Episode: " + episode.getEpisode() + ", Air Date: " + episode.getAirDate()
        );
        repository.insert(favorite);
    }

    public void removeFromFavorites(String itemId) {
        repository.deleteById(itemId);
    }

    public boolean isFavorite(String itemId) {
        return repository.isFavorite(itemId);
    }
}
