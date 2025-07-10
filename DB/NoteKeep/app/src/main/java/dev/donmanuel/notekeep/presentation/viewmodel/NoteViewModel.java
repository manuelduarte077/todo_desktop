package dev.donmanuel.notekeep.presentation.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import dev.donmanuel.notekeep.data.entity.Note;
import dev.donmanuel.notekeep.data.repository.NoteRepository;

/**
 * ViewModel for the Note entity
 * Handles the communication between the UI and the repository
 */
public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository repository;
    private final LiveData<List<Note>> allNotes;

    /**
     * Constructor for the ViewModel
     *
     * @param application The application context
     */
    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    /**
     * Get all notes from the repository
     *
     * @return LiveData list of all notes
     */
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    /**
     * Insert a new note into the repository
     *
     * @param note The note to insert
     */
    public void insert(Note note) {
        repository.insert(note);
    }

    /**
     * Update an existing note in the repository
     *
     * @param note The note to update
     */
    public void update(Note note) {
        repository.update(note);
    }

    /**
     * Delete a note from the repository
     *
     * @param note The note to delete
     */
    public void delete(Note note) {
        repository.delete(note);
    }

    /**
     * Get notes by category from the repository
     *
     * @param category The category to filter by
     * @return LiveData list of notes in the specified category
     */
    public LiveData<List<Note>> getNotesByCategory(String category) {
        return repository.getNotesByCategory(category);
    }

    /**
     * Search notes by title or content
     *
     * @param query The search query
     * @return LiveData list of notes matching the query
     */
    public LiveData<List<Note>> searchNotes(String query) {
        return repository.searchNotes(query);
    }
}
