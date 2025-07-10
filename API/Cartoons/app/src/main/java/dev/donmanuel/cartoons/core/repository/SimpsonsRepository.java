package dev.donmanuel.cartoons.core.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import dev.donmanuel.cartoons.core.api.ApiClient;
import dev.donmanuel.cartoons.core.api.SimpsonsApiService;
import dev.donmanuel.cartoons.model.Simpsons;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpsonsRepository {
    private final SimpsonsApiService simpsonsApiService;
    private final MutableLiveData<List<Simpsons>> simpsonsData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SimpsonsRepository() {
        simpsonsApiService = ApiClient.getClient().create(SimpsonsApiService.class);
        fetchSimpsonsData();
    }

    public LiveData<List<Simpsons>> getSimpsonsData() {
        return simpsonsData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private void fetchSimpsonsData() {
        isLoading.setValue(true);

        Call<List<Simpsons>> call = simpsonsApiService.getSimpsonsData();
        call.enqueue(new Callback<List<Simpsons>>() {
            @Override
            public void onResponse(Call<List<Simpsons>> call, Response<List<Simpsons>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    simpsonsData.setValue(response.body());
                    errorMessage.setValue(null);
                } else {
                    errorMessage.setValue("Error fetching Simpsons data: " + response.message());
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<Simpsons>> call, Throwable t) {
                errorMessage.setValue("Network error: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    public void refreshData() {
        fetchSimpsonsData();
    }
}
