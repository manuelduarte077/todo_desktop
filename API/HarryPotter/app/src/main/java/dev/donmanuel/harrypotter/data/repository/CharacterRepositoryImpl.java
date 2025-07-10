package dev.donmanuel.harrypotter.data.repository;

import java.util.List;

import dev.donmanuel.harrypotter.data.api.ApiClient;
import dev.donmanuel.harrypotter.data.api.HarryPotterApi;
import dev.donmanuel.harrypotter.data.model.Character;
import retrofit2.Call;
import retrofit2.Callback;

public class CharacterRepositoryImpl implements CharacterRepository {

    private final HarryPotterApi apiService;

    public CharacterRepositoryImpl() {
        apiService = ApiClient.getClient().create(HarryPotterApi.class);
    }

    @Override
    public void getAllCharacters(Callback<List<Character>> callback) {
        Call<List<Character>> call = apiService.getAllCharacters();
        call.enqueue(callback);
    }

}
