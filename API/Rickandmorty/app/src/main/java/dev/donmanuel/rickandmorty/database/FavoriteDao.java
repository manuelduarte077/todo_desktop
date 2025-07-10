package dev.donmanuel.rickandmorty.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Query("DELETE FROM favorites WHERE itemId = :itemId")
    void deleteById(String itemId);

    @Query("SELECT * FROM favorites ORDER BY name ASC")
    LiveData<List<Favorite>> getAllFavorites();

    @Query("SELECT * FROM favorites WHERE type = :type ORDER BY name ASC")
    LiveData<List<Favorite>> getFavoritesByType(String type);

    @Query("SELECT COUNT(*) FROM favorites WHERE itemId = :itemId")
    int isFavorite(String itemId);
}
