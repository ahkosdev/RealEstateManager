package fr.kosdev.realestatemanager.Repositories;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

import fr.kosdev.realestatemanager.Database.PropertyDao;
import fr.kosdev.realestatemanager.Database.PropertySimpleSqliteQuery;
import fr.kosdev.realestatemanager.Models.Property;

public class PropertyDataRepository {

    private final PropertyDao propertyDao;

    public PropertyDataRepository(PropertyDao propertyDao) {
        this.propertyDao = propertyDao;
    }
    public void createProperty(Property property){
        propertyDao.createProperty(property);
    }
    public LiveData<List<Property>> getProperties(){
        return propertyDao.getProperties();
    }

    public LiveData<Property> getProperty(long propertyId){
        return propertyDao.getPropertyWithId(propertyId);
    }

    public void updateProperty(Property property){
        propertyDao.updateProperty(property);
    }
    public LiveData<List<Property>> getPropertiesWithPrice(String minPrice, String maxPrice){
        return propertyDao.getPropertiesWithMinAndMaxPrice(minPrice,maxPrice);
    }
    public LiveData<List<Property>> getPropertiesWithFilter(SimpleSQLiteQuery simpleSQLiteQuery){
        return propertyDao.getPropertiesWithFilter(simpleSQLiteQuery);

    }
}
