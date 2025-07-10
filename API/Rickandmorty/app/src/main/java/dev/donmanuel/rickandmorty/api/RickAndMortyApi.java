package dev.donmanuel.rickandmorty.api;

import dev.donmanuel.rickandmorty.model.ApiResponse;
import dev.donmanuel.rickandmorty.model.Character;
import dev.donmanuel.rickandmorty.model.Episode;
import dev.donmanuel.rickandmorty.model.Location;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RickAndMortyApi {

    // Characters endpoints
    @GET("character")
    Call<ApiResponse<Character>> getCharacters();

    @GET("character")
    Call<ApiResponse<Character>> getCharacters(@Query("page") int page);

    @GET("character/{id}")
    Call<Character> getCharacter(@Path("id") int id);

    // Locations endpoints
    @GET("location")
    Call<ApiResponse<Location>> getLocations();

    @GET("location")
    Call<ApiResponse<Location>> getLocations(@Query("page") int page);

    @GET("location/{id}")
    Call<Location> getLocation(@Path("id") int id);

    // Episodes endpoints
    @GET("episode")
    Call<ApiResponse<Episode>> getEpisodes();

    @GET("episode")
    Call<ApiResponse<Episode>> getEpisodes(@Query("page") int page);

    @GET("episode/{id}")
    Call<Episode> getEpisode(@Path("id") int id);
}
