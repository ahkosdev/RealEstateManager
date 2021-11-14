package fr.kosdev.realestatemanager.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import fr.kosdev.realestatemanager.Models.Property;

@Database(entities = Property.class, version = 1, exportSchema = false)
public abstract class RealEstateDatabase extends RoomDatabase{

    public static volatile RealEstateDatabase INSTANCE;
    public abstract PropertyDao propertyDao();

    public static RealEstateDatabase getInstance(Context context){

        if (INSTANCE == null){
            synchronized (RealEstateDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RealEstateDatabase.class, "RealEstateDB").build();
                }
            }
        }
        return INSTANCE;
    }

}
