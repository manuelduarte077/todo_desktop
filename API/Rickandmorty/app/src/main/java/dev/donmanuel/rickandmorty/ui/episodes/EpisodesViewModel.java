package dev.donmanuel.rickandmorty.ui.episodes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.donmanuel.rickandmorty.api.ApiClient;
import dev.donmanuel.rickandmorty.model.ApiResponse;
import dev.donmanuel.rickandmorty.model.Episode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodesViewModel extends ViewModel {

    private final MutableLiveData<List<Episode>> episodes = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private int currentPage = 1;
    private boolean isLastPage = false;

    public EpisodesViewModel() {
        loadEpisodes();
    }

    public LiveData<List<Episode>> getEpisodes() {
        return episodes;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadEpisodes() {
        if (isLastPage) return;

        isLoading.setValue(true);
        error.setValue(null);

        ApiClient.getApiService().getEpisodes(currentPage).enqueue(new Callback<ApiResponse<Episode>>() {
            @Override
            public void onResponse(Call<ApiResponse<Episode>> call, Response<ApiResponse<Episode>> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Episode> apiResponse = response.body();

                    if (currentPage == 1) {
                        episodes.setValue(apiResponse.getResults());
                    } else {
                        List<Episode> currentList = episodes.getValue();
                        if (currentList != null) {
                            currentList.addAll(apiResponse.getResults());
                            episodes.setValue(currentList);
                        } else {
                            episodes.setValue(apiResponse.getResults());
                        }
                    }

                    // Check if this is the last page
                    if (apiResponse.getInfo().getNext() == null) {
                        isLastPage = true;
                    } else {
                        currentPage++;
                    }
                } else {
                    error.setValue("Failed to load episodes");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Episode>> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void refreshEpisodes() {
        currentPage = 1;
        isLastPage = false;
        loadEpisodes();
    }
}
