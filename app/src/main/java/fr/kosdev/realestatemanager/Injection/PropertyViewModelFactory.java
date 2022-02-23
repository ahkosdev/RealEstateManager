package fr.kosdev.realestatemanager.Injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import fr.kosdev.realestatemanager.Controllers.PropertyViewHolder;
import fr.kosdev.realestatemanager.Controllers.PropertyViewModel;
import fr.kosdev.realestatemanager.Repositories.GeocodeRepository;
import fr.kosdev.realestatemanager.Repositories.PropertyDataRepository;

public class PropertyViewModelFactory implements ViewModelProvider.Factory {

    private final PropertyDataRepository propertyDataSource;
    private final Executor executor;
    private final GeocodeRepository geocodeRepository;

    public PropertyViewModelFactory(PropertyDataRepository propertyDataSource, Executor executor, GeocodeRepository geocodeRepository) {
        this.propertyDataSource = propertyDataSource;
        this.executor = executor;
        this.geocodeRepository = geocodeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyViewModel.class)){
            return (T) new PropertyViewModel(propertyDataSource, executor,geocodeRepository);
        }
        throw new IllegalArgumentException("Unknown viewModel class");
    }
}
