package dev.donmanuel.cartoons.core.api;

import java.util.List;

import dev.donmanuel.cartoons.model.Simpsons;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SimpsonsApiService {
    @GET("simpsons/episodes")
    Call<List<Simpsons>> getSimpsonsData();
}
