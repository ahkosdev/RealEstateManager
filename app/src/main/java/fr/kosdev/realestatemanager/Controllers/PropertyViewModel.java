package fr.kosdev.realestatemanager.Controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;
import java.util.concurrent.Executor;

import fr.kosdev.realestatemanager.Database.PropertySimpleSqliteQuery;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.Models.pojo.RealStateGeocode;
import fr.kosdev.realestatemanager.Repositories.GeocodeRepository;
import fr.kosdev.realestatemanager.Repositories.PropertyDataRepository;

public class PropertyViewModel extends ViewModel {

    private final PropertyDataRepository propertyDataSource;
    private final Executor executor;
    private LiveData<List<Property>> properties;
    private MutableLiveData<RealStateGeocode> geocodeLiveData;
    private GeocodeRepository geocodeRepository;

    public PropertyViewModel(PropertyDataRepository propertyDataSource, Executor executor, GeocodeRepository geocodeRepository) {
        this.propertyDataSource = propertyDataSource;
        this.executor = executor;
        this.geocodeRepository = geocodeRepository;
    }

    public void init(){
        if (properties != null){
            return;
        }
        properties = propertyDataSource.getProperties();

       // if (geocodeLiveData != null){
           //return;
        //}
        //geocodeRepository = GeocodeRepository.getInstance();
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

    public LiveData<Property> getProperty(long propertyId){
        return propertyDataSource.getProperty(propertyId);
    }

    public LiveData<RealStateGeocode> getAddressGeocode(String address){

        geocodeLiveData = geocodeRepository.getLocation(address);
        return geocodeLiveData;
    }

    public void updateProperty(Property property){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                propertyDataSource.updateProperty(property);
            }
        });
    }

    public LiveData<List<Property>> getPropertiesWithPrice(String minPrice, String maxPrice){
        return propertyDataSource.getPropertiesWithPrice(minPrice, maxPrice);
    }
    public LiveData<List<Property>> getPropertiesWithFilter(SimpleSQLiteQuery simpleSQLiteQuery){

        return propertyDataSource.getPropertiesWithFilter(simpleSQLiteQuery);
    }


}
