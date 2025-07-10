package dev.donmanuel.notekeep.presentation.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import dev.donmanuel.notekeep.R;
import dev.donmanuel.notekeep.data.entity.Note;

/**
 * Adapter for the RecyclerView that displays the list of notes
 */
public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {

    private final OnNoteClickListener listener;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    /**
     * Interface for handling note click events
     */
    public interface OnNoteClickListener {
        void onNoteClick(Note note);

        void onNoteLongClick(Note note, View view);
    }

    /**
     * Constructor for the adapter
     *
     * @param listener The listener for note click events
     */
    public NoteAdapter(OnNoteClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    /**
     * DiffUtil callback for efficient updates
     */
    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getContent().equals(newItem.getContent()) &&
                    oldItem.getCategory().equals(newItem.getCategory()) &&
                    oldItem.getUpdatedAt().equals(newItem.getUpdatedAt());
        }
    };

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.bind(currentNote);
    }

    /**
     * ViewHolder for the note items
     */
    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView contentTextView;
        private final TextView categoryTextView;
        private final TextView dateTextView;
        private final View categoryIndicator;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_note_title);
            contentTextView = itemView.findViewById(R.id.text_note_content);
            categoryTextView = itemView.findViewById(R.id.text_note_category);
            dateTextView = itemView.findViewById(R.id.text_note_date);
            categoryIndicator = itemView.findViewById(R.id.view_category_indicator);

            // Set click listeners
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onNoteClick(getItem(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onNoteLongClick(getItem(position), v);
                    return true;
                }
                return false;
            });
        }

        /**
         * Bind note data to the views
         *
         * @param note The note to bind
         */
        public void bind(Note note) {
            titleTextView.setText(note.getTitle());
            contentTextView.setText(note.getContent());
            categoryTextView.setText(note.getCategory());

            // Set date text
            dateTextView.setText(getFormattedDate(note.getUpdatedAt()));

            // Set category color
            int colorResId = getCategoryColorResId(note.getCategory());
            int color = ContextCompat.getColor(itemView.getContext(), colorResId);

            // Apply color to category indicator
            categoryIndicator.setBackgroundColor(color);

            // Apply color to category badge
            categoryTextView.setBackgroundTintList(ColorStateList.valueOf(color));
        }

        /**
         * Get the color resource ID for a category
         *
         * @param category The category name
         * @return The color resource ID
         */
        private int getCategoryColorResId(String category) {
            if (category.equals(itemView.getContext().getString(R.string.category_work))) {
                return R.color.category_work;
            } else if (category.equals(itemView.getContext().getString(R.string.category_personal))) {
                return R.color.category_personal;
            } else if (category.equals(itemView.getContext().getString(R.string.category_study))) {
                return R.color.category_study;
            } else {
                return R.color.category_other;
            }
        }

        /**
         * Get a formatted date string
         *
         * @param date The date to format
         * @return The formatted date string
         */
        private String getFormattedDate(Date date) {
            if (date == null) {
                return "";
            }

            long diffInMillis = System.currentTimeMillis() - date.getTime();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

            if (diffInDays == 0) {
                // Today
                return "Hoy";
            } else if (diffInDays == 1) {
                // Yesterday
                return "Ayer";
            } else if (diffInDays < 7) {
                // Less than a week
                return "Hace " + diffInDays + " días";
            } else {
                // More than a week
                return dateFormat.format(date);
            }
        }
    }
}
