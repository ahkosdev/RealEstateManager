package fr.kosdev.realestatemanager.Api;

import com.google.gson.Gson;

import fr.kosdev.realestatemanager.Models.pojo.RealStateGeocode;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingApiCall {

    @GET("api/geocode/json?key=AIzaSyD82JMaZzTg-SzGWcB4vpt_qdKQGQ9JvBo")
    Call<RealStateGeocode> getAddressLocation(@Query("address") String address);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
