package dev.donmanuel.harrypotter.data.repository;

import java.util.List;

import dev.donmanuel.harrypotter.data.model.Character;
import retrofit2.Callback;

public interface CharacterRepository {
    void getAllCharacters(Callback<List<Character>> callback);
}
