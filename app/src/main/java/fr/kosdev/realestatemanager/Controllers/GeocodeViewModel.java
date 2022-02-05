package fr.kosdev.realestatemanager.Controllers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import fr.kosdev.realestatemanager.Models.pojo.RealStateGeocode;
import fr.kosdev.realestatemanager.Repositories.GeocodeRepository;

public class GeocodeViewModel extends ViewModel {

    private MutableLiveData<RealStateGeocode> geocodeLiveData;
    private GeocodeRepository geocodeRepository;

    public void init(){
        if (geocodeLiveData != null){
            return;
        }
        geocodeRepository = GeocodeRepository.getInstance();
    }

    public LiveData<RealStateGeocode> getAddressGeocode(String address){

        geocodeLiveData = geocodeRepository.getLocation(address);
        return geocodeLiveData;
    }
}
