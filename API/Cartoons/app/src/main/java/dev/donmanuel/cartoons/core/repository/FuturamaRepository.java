package dev.donmanuel.cartoons.core.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import dev.donmanuel.cartoons.core.api.ApiClient;
import dev.donmanuel.cartoons.core.api.FuturamaApiService;
import dev.donmanuel.cartoons.model.Futurama;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuturamaRepository {
    private final FuturamaApiService futuramaApiService;
    private final MutableLiveData<List<Futurama>> futuramaData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public FuturamaRepository() {
        futuramaApiService = ApiClient.getClient().create(FuturamaApiService.class);
        fetchFuturamaData();
    }

    public LiveData<List<Futurama>> getFuturamaData() {
        return futuramaData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private void fetchFuturamaData() {
        isLoading.setValue(true);

        Call<List<Futurama>> call = futuramaApiService.getFuturamaData();
        call.enqueue(new Callback<List<Futurama>>() {
            @Override
            public void onResponse(Call<List<Futurama>> call, Response<List<Futurama>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    futuramaData.setValue(response.body());
                    errorMessage.setValue(null);
                } else {
                    errorMessage.setValue("Error fetching Futurama data: " + response.message());
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<Futurama>> call, Throwable t) {
                errorMessage.setValue("Network error: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    public void refreshData() {
        fetchFuturamaData();
    }
}
