package fr.kosdev.realestatemanager.Injection;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.kosdev.realestatemanager.Database.RealEstateDatabase;
import fr.kosdev.realestatemanager.Repositories.GeocodeRepository;
import fr.kosdev.realestatemanager.Repositories.PropertyDataRepository;

public class Injection {

    public static PropertyDataRepository providePropertyDataSource(Context context){
        RealEstateDatabase database = RealEstateDatabase.getInstance(context);
        return new PropertyDataRepository(database.propertyDao());
    }
    public static Executor provideExecutor(){
        return Executors.newSingleThreadExecutor();
    }
    public static GeocodeRepository provideGeocodeRepository(){
        return GeocodeRepository.getInstance();
    }
    public static PropertyViewModelFactory providePropertyViewModelFactory(Context context){
        PropertyDataRepository dataSource = providePropertyDataSource(context);
        Executor executor = provideExecutor();
        GeocodeRepository geocodeRepository = provideGeocodeRepository();
        return new PropertyViewModelFactory(dataSource,executor,provideGeocodeRepository());
    }
}
