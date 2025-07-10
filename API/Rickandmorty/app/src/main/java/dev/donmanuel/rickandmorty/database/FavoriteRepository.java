package dev.donmanuel.rickandmorty.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FavoriteRepository {

    private final FavoriteDao favoriteDao;
    private final LiveData<List<Favorite>> allFavorites;
    private final LiveData<List<Favorite>> characterFavorites;
    private final LiveData<List<Favorite>> locationFavorites;
    private final LiveData<List<Favorite>> episodeFavorites;

    public FavoriteRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        favoriteDao = db.favoriteDao();
        allFavorites = favoriteDao.getAllFavorites();
        characterFavorites = favoriteDao.getFavoritesByType("character");
        locationFavorites = favoriteDao.getFavoritesByType("location");
        episodeFavorites = favoriteDao.getFavoritesByType("episode");
    }

    // Room executes all queries on a separate thread
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

    // Must be called on a non-UI thread
    public void insert(Favorite favorite) {
        AppDatabase.databaseWriteExecutor.execute(() -> favoriteDao.insert(favorite));
    }

    public void delete(Favorite favorite) {
        AppDatabase.databaseWriteExecutor.execute(() -> favoriteDao.delete(favorite));
    }

    public void deleteById(String itemId) {
        AppDatabase.databaseWriteExecutor.execute(() -> favoriteDao.deleteById(itemId));
    }

    public boolean isFavorite(String itemId) {
        // This is a blocking call, should be used carefully
        final boolean[] result = new boolean[1];
        Thread thread = new Thread(() -> {
            result[0] = favoriteDao.isFavorite(itemId) > 0;
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }
}
