package dev.donmanuel.notekeep.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.donmanuel.notekeep.data.dao.NoteDao;
import dev.donmanuel.notekeep.data.entity.Note;
import dev.donmanuel.notekeep.data.util.DateConverter;

/**
 * Room database for the app
 */
@Database(entities = {Note.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;
    private static final String DATABASE_NAME = "note_database";
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Get the DAO for the Note entity
     *
     * @return The DAO
     */
    public abstract NoteDao noteDao();

    /**
     * Get the singleton instance of the database
     *
     * @param context The application context
     * @return The database instance
     */
    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    /**
     * Callback for database creation
     */
    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to populate the database with initial data when it's created,
            // you can do it here using the databaseWriteExecutor
            /*
            databaseWriteExecutor.execute(() -> {
                NoteDao dao = instance.noteDao();
                
                // Add sample notes
                Note note1 = new Note("Reunión de trabajo", "Discutir el proyecto NoteKeep", "Trabajo");
                Note note2 = new Note("Compras", "Leche, pan, huevos", "Personal");
                Note note3 = new Note("Estudiar Android", "Repasar Room y LiveData", "Estudio");
                
                dao.insert(note1);
                dao.insert(note2);
                dao.insert(note3);
            });
            */
        }
    };
}
