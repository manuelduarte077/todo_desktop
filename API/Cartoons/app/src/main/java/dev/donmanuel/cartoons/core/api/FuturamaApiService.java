package dev.donmanuel.cartoons.core.api;

import java.util.List;

import dev.donmanuel.cartoons.model.Futurama;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FuturamaApiService {
    @GET("futurama/inventory")
    Call<List<Futurama>> getFuturamaData();
}
