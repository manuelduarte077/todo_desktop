package dev.donmanuel.notekeep.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dev.donmanuel.notekeep.data.entity.Note;

/**
 * Data Access Object for the Note entity
 */
@Dao
public interface NoteDao {

    /**
     * Insert a new note into the database
     *
     * @param note The note to be inserted
     * @return The ID of the inserted note
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Note note);

    /**
     * Update an existing note
     *
     * @param note The note to be updated
     */
    @Update
    void update(Note note);

    /**
     * Delete a note from the database
     *
     * @param note The note to be deleted
     */
    @Delete
    void delete(Note note);

    /**
     * Get all notes from the database
     *
     * @return LiveData list of all notes
     */
    @Query("SELECT * FROM note_table ORDER BY updated_at DESC")
    LiveData<List<Note>> getAllNotes();

    /**
     * Get notes by category
     *
     * @param category The category to filter by
     * @return LiveData list of notes with the specified category
     */
    @Query("SELECT * FROM note_table WHERE category = :category ORDER BY updated_at DESC")
    LiveData<List<Note>> getNotesByCategory(String category);

    /**
     * Get a note by ID
     *
     * @param id The ID of the note to retrieve
     * @return The note with the specified ID
     */
    @Query("SELECT * FROM note_table WHERE id = :id")
    LiveData<Note> getNoteById(int id);

    /**
     * Search notes by title or content
     *
     * @param query The search query
     * @return LiveData list of notes matching the query
     */
    @Query("SELECT * FROM note_table WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY updated_at DESC")
    LiveData<List<Note>> searchNotes(String query);
}
