package dev.donmanuel.cartoons.core.repository;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.donmanuel.cartoons.core.database.FavoriteDao;
import dev.donmanuel.cartoons.model.Favorite;
import dev.donmanuel.cartoons.model.Futurama;
import dev.donmanuel.cartoons.model.Simpsons;

public class FavoriteRepository {

    private final FavoriteDao favoriteDao;
    private final ExecutorService executorService;
    private final LiveData<List<Favorite>> allFavorites;
    private final LiveData<List<Favorite>> simpsonsFavorites;
    private final LiveData<List<Favorite>> futuramaFavorites;

    public FavoriteRepository(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
        this.executorService = Executors.newSingleThreadExecutor();

        allFavorites = favoriteDao.getAllFavorites();
        simpsonsFavorites = favoriteDao.getFavoritesByType(Favorite.TYPE_SIMPSONS);
        futuramaFavorites = favoriteDao.getFavoritesByType(Favorite.TYPE_FUTURAMA);
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

    public void addToFavorites(final Simpsons simpsons) {
        executorService.execute(() -> {
            Favorite favorite = Favorite.fromSimpsons(simpsons);
            favoriteDao.insertFavorite(favorite);
        });
    }

    public void addToFavorites(final Futurama futurama) {
        executorService.execute(() -> {
            Favorite favorite = Favorite.fromFuturama(futurama);
            favoriteDao.insertFavorite(favorite);
        });
    }

    public void removeFromFavorites(final Simpsons simpsons) {
        executorService.execute(() ->
                favoriteDao.deleteFavoriteByItemIdAndType(simpsons.getId(), Favorite.TYPE_SIMPSONS)
        );
    }

    public void removeFromFavorites(final Futurama futurama) {
        executorService.execute(() ->
                favoriteDao.deleteFavoriteByItemIdAndType(futurama.getId(), Favorite.TYPE_FUTURAMA)
        );
    }

    public void removeFavorite(final Favorite favorite) {
        executorService.execute(() ->
                favoriteDao.deleteFavorite(favorite)
        );
    }

    public boolean isSimpsonsInFavorites(final int simpsonsId) {
        // This is a blocking call, should be used with caution
        try {
            return favoriteDao.isFavorite(simpsonsId, Favorite.TYPE_SIMPSONS);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFuturamaInFavorites(final int futuramaId) {
        // This is a blocking call, should be used with caution
        try {
            return favoriteDao.isFavorite(futuramaId, Favorite.TYPE_FUTURAMA);
        } catch (Exception e) {
            return false;
        }
    }
}
