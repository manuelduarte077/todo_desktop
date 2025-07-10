package dev.donmanuel.harrypotter.presentation.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dev.donmanuel.harrypotter.data.repository.CharacterRepository;
import dev.donmanuel.harrypotter.data.repository.CharacterRepositoryImpl;

public class CharacterViewModelFactory implements ViewModelProvider.Factory {

    private final CharacterRepository repository;

    public CharacterViewModelFactory() {
        this.repository = new CharacterRepositoryImpl();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CharacterViewModel.class)) {
            return (T) new CharacterViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
