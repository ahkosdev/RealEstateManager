package fr.kosdev.realestatemanager.Injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import fr.kosdev.realestatemanager.Controllers.PropertyViewHolder;
import fr.kosdev.realestatemanager.Controllers.PropertyViewModel;
import fr.kosdev.realestatemanager.Repositories.PropertyDataRepository;

public class PropertyViewModelFactory implements ViewModelProvider.Factory {

    private final PropertyDataRepository propertyDataSource;
    private final Executor executor;

    public PropertyViewModelFactory(PropertyDataRepository propertyDataSource, Executor executor) {
        this.propertyDataSource = propertyDataSource;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyViewModel.class)){
            return (T) new PropertyViewModel(propertyDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown viewModel class");
    }
}
