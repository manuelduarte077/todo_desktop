package dev.donmanuel.cartoons.ui.favorites;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import dev.donmanuel.cartoons.core.database.AppDatabase;
import dev.donmanuel.cartoons.core.repository.FavoriteRepository;
import dev.donmanuel.cartoons.model.Favorite;

public class FavoritesViewModel extends AndroidViewModel {

    public static final String FILTER_ALL = "all";
    public static final String FILTER_SIMPSONS = Favorite.TYPE_SIMPSONS;
    public static final String FILTER_FUTURAMA = Favorite.TYPE_FUTURAMA;

    private final FavoriteRepository repository;
    private final LiveData<List<Favorite>> allFavorites;
    private final LiveData<List<Favorite>> simpsonsFavorites;
    private final LiveData<List<Favorite>> futuramaFavorites;

    private final MutableLiveData<String> currentFilter;
    private final MutableLiveData<String> text;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getDatabase(application);
        repository = new FavoriteRepository(database.favoriteDao());

        allFavorites = repository.getAllFavorites();
        simpsonsFavorites = repository.getSimpsonsFavorites();
        futuramaFavorites = repository.getFuturamaFavorites();

        currentFilter = new MutableLiveData<>(FILTER_ALL);
        text = new MutableLiveData<>("Mis Favoritos");
    }

    public LiveData<List<Favorite>> getAllFavorites() {
        return allFavorites;
    }

    public LiveData<List<Favorite>> getSimpsonsFavorites() {
        return simpsonsFavorites;
    }

    public LiveData<List<Favorite>> getFuturamaFavorites() {
        return futuramaFavorites;
    }

    public LiveData<String> getCurrentFilter() {
        return currentFilter;
    }

    public void setFilter(String filter) {
        currentFilter.setValue(filter);
    }

    public LiveData<String> getText() {
        return text;
    }

    public void removeFavorite(Favorite favorite) {
        repository.removeFavorite(favorite);
    }
}
