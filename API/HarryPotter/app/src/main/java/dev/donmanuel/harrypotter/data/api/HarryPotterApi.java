package dev.donmanuel.harrypotter.data.api;

import java.util.List;

import dev.donmanuel.harrypotter.data.model.Character;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HarryPotterApi {

    @GET("characters")
    Call<List<Character>> getAllCharacters();

    @GET("characters/house/{house}")
    Call<List<Character>> getCharactersByHouse(@Path("house") String house);

    @GET("character/{id}")
    Call<List<Character>> getCharacterById(@Path("id") String id);
}
