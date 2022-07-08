package fr.kosdev.realestatemanager.Database;


import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.ArrayList;
import java.util.List;

import fr.kosdev.realestatemanager.Models.Property;

@Dao
public interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createProperty(Property property);

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getProperties();

    @Query("SELECT * FROM Property WHERE id = :propertyId")
    LiveData<Property> getPropertyWithId(long propertyId);

    @Update
    int updateProperty(Property property);

    @Query("SELECT * FROM Property WHERE price >= :minPrice OR price <= :maxPrice")
    LiveData<List<Property>> getPropertiesWithMinAndMaxPrice(String minPrice, String maxPrice);

    @RawQuery(observedEntities = Property.class)
    LiveData<List<Property>> getPropertiesWithFilter(SupportSQLiteQuery query);


}
