package dev.donmanuel.rickandmorty.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://rickandmortyapi.com/api/";
    private static Retrofit retrofit = null;
    private static RickAndMortyApi apiService = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static RickAndMortyApi getApiService() {
        if (apiService == null) {
            apiService = getClient().create(RickAndMortyApi.class);
        }
        return apiService;
    }
}
