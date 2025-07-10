package dev.donmanuel.notekeep.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Entity representing a note in the database
 */
@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String content;

    private String category;

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    /**
     * Constructor for creating a new note
     *
     * @param title    The title of the note
     * @param content  The content of the note
     * @param category The category of the note
     */
    public Note(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    /**
     * Get the ID of the note
     *
     * @return The ID
     */
    public int getId() {
        return id;
    }

    /**
     * Set the ID of the note
     *
     * @param id The ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the title of the note
     *
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the note
     *
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the content of the note
     *
     * @return The content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content of the note
     *
     * @param content The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the category of the note
     *
     * @return The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category of the note
     *
     * @param category The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the creation date of the note
     *
     * @return The creation date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Set the creation date of the note
     *
     * @param createdAt The creation date
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get the last update date of the note
     *
     * @return The last update date
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Set the last update date of the note
     *
     * @param updatedAt The last update date
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
