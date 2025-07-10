package dev.donmanuel.rickandmorty.ui.locations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.donmanuel.rickandmorty.api.ApiClient;
import dev.donmanuel.rickandmorty.model.ApiResponse;
import dev.donmanuel.rickandmorty.model.Location;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationsViewModel extends ViewModel {

    private final MutableLiveData<List<Location>> locations = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private int currentPage = 1;
    private boolean isLastPage = false;

    public LocationsViewModel() {
        loadLocations();
    }

    public LiveData<List<Location>> getLocations() {
        return locations;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadLocations() {
        if (isLastPage) return;

        isLoading.setValue(true);
        error.setValue(null);

        ApiClient.getApiService().getLocations(currentPage).enqueue(new Callback<ApiResponse<Location>>() {
            @Override
            public void onResponse(Call<ApiResponse<Location>> call, Response<ApiResponse<Location>> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Location> apiResponse = response.body();

                    if (currentPage == 1) {
                        locations.setValue(apiResponse.getResults());
                    } else {
                        List<Location> currentList = locations.getValue();
                        if (currentList != null) {
                            currentList.addAll(apiResponse.getResults());
                            locations.setValue(currentList);
                        } else {
                            locations.setValue(apiResponse.getResults());
                        }
                    }

                    // Check if this is the last page
                    if (apiResponse.getInfo().getNext() == null) {
                        isLastPage = true;
                    } else {
                        currentPage++;
                    }
                } else {
                    error.setValue("Failed to load locations");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Location>> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void refreshLocations() {
        currentPage = 1;
        isLastPage = false;
        loadLocations();
    }
}
