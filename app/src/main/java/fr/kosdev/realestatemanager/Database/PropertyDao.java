package fr.kosdev.realestatemanager.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fr.kosdev.realestatemanager.Models.Property;

@Dao
public interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createProperty(Property property);

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getProperties();
}
