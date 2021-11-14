package fr.kosdev.realestatemanager.Controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.Repositories.PropertyDataRepository;

public class PropertyViewModel extends ViewModel {

    private final PropertyDataRepository propertyDataSource;
    private final Executor executor;
    private LiveData<List<Property>> properties;

    public PropertyViewModel(PropertyDataRepository propertyDataSource, Executor executor) {
        this.propertyDataSource = propertyDataSource;
        this.executor = executor;
    }

    public void init(){
        if (properties != null){
            return;
        }
        properties = propertyDataSource.getProperties();
    }
    public LiveData<List<Property>> getProperties(){
        return propertyDataSource.getProperties();
    }
    public void createProperty(Property property){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                propertyDataSource.createProperty(property);
            }
        });
    }
}
