package fr.kosdev.realestatemanager.Repositories;

import androidx.lifecycle.MutableLiveData;

import fr.kosdev.realestatemanager.Api.GeocodingApiCall;
import fr.kosdev.realestatemanager.Models.pojo.RealStateGeocode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeocodeRepository {

    private static GeocodeRepository geocodeRepository;
    public static GeocodeRepository getInstance(){

        if (geocodeRepository==null){
            geocodeRepository = new GeocodeRepository();
        }
        return geocodeRepository;
    }

    private GeocodingApiCall geocodeApi;

    public GeocodeRepository() {
        geocodeApi = GeocodingApiCall.retrofit.create(GeocodingApiCall.class);
    }

    public MutableLiveData<RealStateGeocode> getLocation(String address){
        MutableLiveData<RealStateGeocode> addressLocation = new MutableLiveData<>();
        geocodeApi.getAddressLocation(address).enqueue(new Callback<RealStateGeocode>() {
            @Override
            public void onResponse(Call<RealStateGeocode> call, Response<RealStateGeocode> response) {
                if (response.isSuccessful()){
                    addressLocation.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<RealStateGeocode> call, Throwable t) {
                addressLocation.setValue(null);
            }
        });
        return addressLocation;
    }
}
