package dev.donmanuel.notekeep.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.donmanuel.notekeep.data.dao.NoteDao;
import dev.donmanuel.notekeep.data.database.NoteDatabase;
import dev.donmanuel.notekeep.data.entity.Note;

/**
 * Repository for handling data operations on notes
 */
public class NoteRepository {

    private final NoteDao noteDao;
    private final LiveData<List<Note>> allNotes;
    private final ExecutorService executorService;

    /**
     * Constructor for the repository
     *
     * @param application The application context
     */
    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
        executorService = Executors.newFixedThreadPool(4);
    }

    /**
     * Get all notes
     *
     * @return LiveData list of all notes
     */
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    /**
     * Get notes by category
     *
     * @param category The category to filter by
     * @return LiveData list of notes with the specified category
     */
    public LiveData<List<Note>> getNotesByCategory(String category) {
        return noteDao.getNotesByCategory(category);
    }

    /**
     * Get a note by ID
     *
     * @param id The ID of the note to retrieve
     * @return The note with the specified ID
     */
    public LiveData<Note> getNoteById(int id) {
        return noteDao.getNoteById(id);
    }

    /**
     * Search notes by title or content
     *
     * @param query The search query
     * @return LiveData list of notes matching the search query
     */
    public LiveData<List<Note>> searchNotes(String query) {
        return noteDao.searchNotes(query);
    }

    /**
     * Insert a new note
     *
     * @param note The note to be inserted
     */
    public void insert(Note note) {
        executorService.execute(() -> noteDao.insert(note));
    }

    /**
     * Update an existing note
     *
     * @param note The note to be updated
     */
    public void update(Note note) {
        executorService.execute(() -> noteDao.update(note));
    }

    /**
     * Delete a note
     *
     * @param note The note to be deleted
     */
    public void delete(Note note) {
        executorService.execute(() -> noteDao.delete(note));
    }
}
