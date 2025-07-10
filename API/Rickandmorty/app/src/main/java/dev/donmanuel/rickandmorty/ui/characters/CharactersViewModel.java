package dev.donmanuel.rickandmorty.ui.characters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.donmanuel.rickandmorty.api.ApiClient;
import dev.donmanuel.rickandmorty.model.ApiResponse;
import dev.donmanuel.rickandmorty.model.Character;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharactersViewModel extends ViewModel {

    private final MutableLiveData<List<Character>> characters = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private int currentPage = 1;
    private boolean isLastPage = false;

    public CharactersViewModel() {
        loadCharacters();
    }

    public LiveData<List<Character>> getCharacters() {
        return characters;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadCharacters() {
        if (isLastPage) return;

        isLoading.setValue(true);
        error.setValue(null);

        ApiClient.getApiService().getCharacters(currentPage).enqueue(new Callback<ApiResponse<Character>>() {
            @Override
            public void onResponse(Call<ApiResponse<Character>> call, Response<ApiResponse<Character>> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Character> apiResponse = response.body();

                    if (currentPage == 1) {
                        characters.setValue(apiResponse.getResults());
                    } else {
                        List<Character> currentList = characters.getValue();
                        if (currentList != null) {
                            currentList.addAll(apiResponse.getResults());
                            characters.setValue(currentList);
                        } else {
                            characters.setValue(apiResponse.getResults());
                        }
                    }

                    // Check if this is the last page
                    if (apiResponse.getInfo().getNext() == null) {
                        isLastPage = true;
                    } else {
                        currentPage++;
                    }
                } else {
                    error.setValue("Failed to load characters");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Character>> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void refreshCharacters() {
        currentPage = 1;
        isLastPage = false;
        loadCharacters();
    }
}
