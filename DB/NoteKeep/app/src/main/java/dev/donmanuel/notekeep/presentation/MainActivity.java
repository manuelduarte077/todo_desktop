package dev.donmanuel.notekeep.presentation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

import dev.donmanuel.notekeep.R;
import dev.donmanuel.notekeep.data.entity.Note;
import dev.donmanuel.notekeep.presentation.adapter.NoteAdapter;
import dev.donmanuel.notekeep.presentation.util.SpacingItemDecoration;
import dev.donmanuel.notekeep.presentation.viewmodel.NoteViewModel;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private TextView emptyStateTextView;
    private BottomSheetDialog bottomSheetDialog;
    private Note currentNote;
    private String currentCategory = null; // Para filtrado por categoría

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializar vistas
        recyclerView = findViewById(R.id.recycler_notes);
        emptyStateTextView = findViewById(R.id.text_empty_state);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Agregar espaciado entre elementos
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));

        // Inicializar adapter con la implementación correcta de OnNoteClickListener
        adapter = new NoteAdapter(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                MainActivity.this.onNoteClick(note);
            }

            @Override
            public void onNoteLongClick(Note note, View view) {
                MainActivity.this.onNoteLongClick(note, view);
            }
        });
        recyclerView.setAdapter(adapter);

        // Inicializar ViewModel
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // Asegurarse de que el RecyclerView sea visible
        recyclerView.setVisibility(View.VISIBLE);
        emptyStateTextView.setVisibility(View.GONE);

        // Observar notas
        observeNotes();

        // Configurar FAB
        FloatingActionButton fabAddNote = findViewById(R.id.fab_add_note);
        fabAddNote.setOnClickListener(v -> showBottomSheet(null));

        // Configurar chips de categoría
        setupCategoryChips();

        // Crear una nota de prueba si no hay notas
        noteViewModel.getAllNotes().observe(this, notes -> {
            if (notes.isEmpty()) {
                createSampleNote();
            }
        });
    }

    /**
     * Crear una nota de prueba
     */
    private void createSampleNote() {
        Note sampleNote = new Note(
                "Bienvenido a NoteKeep",
                "Esta es una aplicación para tomar notas. Puedes crear, editar y eliminar notas. También puedes filtrar las notas por categoría.",
                getString(R.string.category_personal)
        );
        noteViewModel.insert(sampleNote);
    }

    /**
     * Observar notas desde el ViewModel
     */
    private void observeNotes() {
        // Remover observadores anteriores para evitar duplicados
        if (currentCategory != null) {
            noteViewModel.getAllNotes().removeObservers(this);
            // Observar notas por categoría
            noteViewModel.getNotesByCategory(currentCategory).observe(this, this::updateNotesList);
        } else {
            // Remover observadores de categorías específicas
            noteViewModel.getNotesByCategory(getString(R.string.category_work)).removeObservers(this);
            noteViewModel.getNotesByCategory(getString(R.string.category_personal)).removeObservers(this);
            noteViewModel.getNotesByCategory(getString(R.string.category_study)).removeObservers(this);
            noteViewModel.getNotesByCategory(getString(R.string.category_other)).removeObservers(this);

            // Observar todas las notas
            noteViewModel.getAllNotes().observe(this, this::updateNotesList);
        }
    }

    /**
     * Actualizar la lista de notas en el RecyclerView
     *
     * @param notes La lista de notas a mostrar
     */
    private void updateNotesList(java.util.List<Note> notes) {
        adapter.submitList(notes);

        // Mostrar el estado vacío solo si no hay notas
        if (notes == null || notes.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Configurar funcionalidad de búsqueda
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_notes));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchNotes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    observeNotes(); // Restablecer a todas las notas o categoría actual
                } else {
                    searchNotes(newText);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            // Implementar lógica de ordenación aquí
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Buscar notas por título o contenido
     *
     * @param query La consulta de búsqueda
     */
    private void searchNotes(String query) {
        noteViewModel.searchNotes(query).observe(this, this::updateNotesList);
    }

    /**
     * Mostrar bottom sheet para agregar o editar una nota
     *
     * @param note La nota a editar, o null si se está agregando una nueva nota
     */
    private void showBottomSheet(Note note) {
        currentNote = note;

        // Crear diálogo bottom sheet
        bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_note, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Inicializar vistas
        TextView titleTextView = bottomSheetView.findViewById(R.id.text_bottom_sheet_title);
        TextInputEditText titleEditText = bottomSheetView.findViewById(R.id.edit_note_title);
        TextInputEditText contentEditText = bottomSheetView.findViewById(R.id.edit_note_content);
        AutoCompleteTextView categoryDropdown = bottomSheetView.findViewById(R.id.dropdown_category);

        // Configurar dropdown de categorías
        String[] categories = {
                getString(R.string.category_work),
                getString(R.string.category_personal),
                getString(R.string.category_study),
                getString(R.string.category_other)
        };
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, categories);
        categoryDropdown.setAdapter(categoryAdapter);

        // Configurar botón de guardar
        bottomSheetView.findViewById(R.id.button_save_note).setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String content = contentEditText.getText().toString().trim();
            String category = categoryDropdown.getText().toString().trim();

            // Validar entrada
            if (title.isEmpty()) {
                titleEditText.setError(getString(R.string.error_empty_title));
                return;
            }
            if (category.isEmpty()) {
                categoryDropdown.setError(getString(R.string.error_empty_category));
                return;
            }

            // Guardar nota
            if (currentNote == null) {
                // Crear nueva nota
                Note newNote = new Note(title, content, category);
                noteViewModel.insert(newNote);
                Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show();
            } else {
                // Actualizar nota existente
                currentNote.setTitle(title);
                currentNote.setContent(content);
                currentNote.setCategory(category);
                currentNote.setUpdatedAt(new Date());
                noteViewModel.update(currentNote);
                Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show();
            }

            bottomSheetDialog.dismiss();
        });

        // Si se está editando una nota existente, llenar los campos
        if (note != null) {
            titleTextView.setText(R.string.edit_note);
            titleEditText.setText(note.getTitle());
            contentEditText.setText(note.getContent());
            categoryDropdown.setText(note.getCategory(), false);
        }

        // Mostrar bottom sheet
        bottomSheetDialog.show();

        // Establecer bottom sheet en estado expandido
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onNoteClick(Note note) {
        showBottomSheet(note);
    }

    @Override
    public void onNoteLongClick(Note note, View view) {
        // Mostrar un menú emergente para la opción de eliminar
        android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(this, view);
        popupMenu.getMenu().add(getString(R.string.delete_note));
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals(getString(R.string.delete_note))) {
                noteViewModel.delete(note);
                Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    /**
     * Configurar los chips de categoría
     */
    private void setupCategoryChips() {
        // Chip para todas las notas
        Chip chipAll = findViewById(R.id.chip_all);
        chipAll.setOnClickListener(v -> {
            currentCategory = null;
            observeNotes();
            updateChipSelection(chipAll);
        });

        // Chip para categoría Trabajo
        Chip chipWork = findViewById(R.id.chip_work);
        chipWork.setOnClickListener(v -> {
            currentCategory = getString(R.string.category_work);
            observeNotes();
            updateChipSelection(chipWork);
        });

        // Chip para categoría Personal
        Chip chipPersonal = findViewById(R.id.chip_personal);
        chipPersonal.setOnClickListener(v -> {
            currentCategory = getString(R.string.category_personal);
            observeNotes();
            updateChipSelection(chipPersonal);
        });

        // Chip para categoría Estudio
        Chip chipStudy = findViewById(R.id.chip_study);
        chipStudy.setOnClickListener(v -> {
            currentCategory = getString(R.string.category_study);
            observeNotes();
            updateChipSelection(chipStudy);
        });

        // Seleccionar el chip "Todas" por defecto
        chipAll.setChecked(true);
    }

    /**
     * Actualizar la selección de chips
     *
     * @param selectedChip El chip seleccionado
     */
    private void updateChipSelection(Chip selectedChip) {
        // Desmarcar todos los chips
        ((Chip) findViewById(R.id.chip_all)).setChecked(false);
        ((Chip) findViewById(R.id.chip_work)).setChecked(false);
        ((Chip) findViewById(R.id.chip_personal)).setChecked(false);
        ((Chip) findViewById(R.id.chip_study)).setChecked(false);

        // Marcar el chip seleccionado
        selectedChip.setChecked(true);
    }
}
