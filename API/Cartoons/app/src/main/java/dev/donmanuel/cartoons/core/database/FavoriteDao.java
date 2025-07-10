package dev.donmanuel.cartoons.core.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import dev.donmanuel.cartoons.model.Favorite;

@Dao
public interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFavorite(Favorite favorite);

    @Delete
    void deleteFavorite(Favorite favorite);

    @Query("DELETE FROM favorites WHERE itemId = :itemId AND itemType = :itemType")
    void deleteFavoriteByItemIdAndType(int itemId, String itemType);

    @Query("SELECT * FROM favorites ORDER BY favoriteId DESC")
    LiveData<List<Favorite>> getAllFavorites();

    @Query("SELECT * FROM favorites WHERE itemType = :itemType ORDER BY favoriteId DESC")
    LiveData<List<Favorite>> getFavoritesByType(String itemType);

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = :itemId AND itemType = :itemType)")
    boolean isFavorite(int itemId, String itemType);
}
