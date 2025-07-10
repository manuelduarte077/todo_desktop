package dev.donmanuel.harrypotter.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.donmanuel.harrypotter.data.model.Character;
import dev.donmanuel.harrypotter.data.repository.CharacterRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharacterViewModel extends ViewModel {
    
    private final CharacterRepository repository;
    private final MutableLiveData<List<Character>> charactersLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    
    public CharacterViewModel(CharacterRepository repository) {
        this.repository = repository;
    }
    
    public LiveData<List<Character>> getCharacters() {
        return charactersLiveData;
    }
    
    public LiveData<Boolean> getLoading() {
        return loadingLiveData;
    }
    
    public LiveData<String> getError() {
        return errorLiveData;
    }
    
    public void loadAllCharacters() {
        loadingLiveData.setValue(true);
        repository.getAllCharacters(new Callback<>() {
            @Override
            public void onResponse(Call<List<Character>> call, Response<List<Character>> response) {
                loadingLiveData.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    charactersLiveData.setValue(response.body());
                } else {
                    errorLiveData.setValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Character>> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }

}
