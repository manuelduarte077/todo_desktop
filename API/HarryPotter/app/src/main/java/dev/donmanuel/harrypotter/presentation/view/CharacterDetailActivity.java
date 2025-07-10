package dev.donmanuel.harrypotter.presentation.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import dev.donmanuel.harrypotter.R;
import dev.donmanuel.harrypotter.data.model.Character;
import dev.donmanuel.harrypotter.data.model.Wand;

public class CharacterDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CHARACTER = "extra_character";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);

        // Get character from intent
        Character character = (Character) getIntent().getSerializableExtra(EXTRA_CHARACTER);
        if (character == null) {
            finish();
            return;
        }

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setup collapsing toolbar
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle(character.getName());

        // Display character image
        ImageView imageCharacter = findViewById(R.id.image_character_detail);
        if (character.getImage() != null && !character.getImage().isEmpty()) {
            Glide.with(this)
                    .load(character.getImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder_character)
                            .error(R.drawable.placeholder_character))
                    .into(imageCharacter);
        } else {
            imageCharacter.setImageResource(R.drawable.placeholder_character);
        }

        // Display character details
        TextView textName = findViewById(R.id.text_name_detail);
        TextView textHouse = findViewById(R.id.text_house_detail);
        TextView textSpecies = findViewById(R.id.text_species_detail);
        TextView textActor = findViewById(R.id.text_actor_detail);
        TextView textWandWood = findViewById(R.id.text_wand_wood);
        TextView textWandCore = findViewById(R.id.text_wand_core);
        TextView textWandLength = findViewById(R.id.text_wand_length);
        TextView textPatronus = findViewById(R.id.text_patronus);

        textName.setText(character.getName());
        textHouse.setText(character.getHouse() != null && !character.getHouse().isEmpty() ?
                character.getHouse() : "Unknown");
        textSpecies.setText(character.getSpecies() != null && !character.getSpecies().isEmpty() ?
                character.getSpecies() : "Unknown");
        textActor.setText(character.getActor() != null && !character.getActor().isEmpty() ?
                character.getActor() : "Unknown");

        // Handle wand information
        Wand wand = character.getWand();
        if (wand != null) {
            textWandWood.setText(wand.getWood() != null && !wand.getWood().isEmpty() ?
                    wand.getWood() : "Unknown");
            textWandCore.setText(wand.getCore() != null && !wand.getCore().isEmpty() ?
                    wand.getCore() : "Unknown");
            textWandLength.setText(wand.getLength() != null ?
                    wand.getLength() + " inches" : "Unknown");
        } else {
            textWandWood.setText("Unknown");
            textWandCore.setText("Unknown");
            textWandLength.setText("Unknown");
        }

        textPatronus.setText(character.getPatronus() != null && !character.getPatronus().isEmpty() ?
                character.getPatronus() : "Unknown");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
