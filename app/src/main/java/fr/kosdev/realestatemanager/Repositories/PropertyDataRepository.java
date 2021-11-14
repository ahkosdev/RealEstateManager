package fr.kosdev.realestatemanager.Repositories;

import androidx.lifecycle.LiveData;

import java.util.List;

import fr.kosdev.realestatemanager.Database.PropertyDao;
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
}
