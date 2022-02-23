package fr.kosdev.realestatemanager.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import fr.kosdev.realestatemanager.Controllers.Fragments.HomePageFragment;
import fr.kosdev.realestatemanager.Controllers.PropertyViewModel;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient mLocationProviderClient;
    private List<Property> mProperties = HomePageFragment.mPropertyList;
    private PropertyViewModel propertyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
    }

    private void configurePropertiesMap(){
        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.property_map);
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED){
            getPropertiesNearbyAgentLocation();

        }else {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 40);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 40){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getPropertiesNearbyAgentLocation();
            }
        }
    }

    private void getPropertiesNearbyAgentLocation(){

        Task<Location> task = mLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null){
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            mMap = googleMap;

                            for (int i = 0 ; i < mProperties.size() ; i++){

                                propertyViewModel.getAddressGeocode(mProperties.get(i).getAddress()).observe(mapFragment.getViewLifecycleOwner(), realStateGeocode -> {

                                    try {

                                        mMap.clear();

                                        double lat = realStateGeocode.getResults().get(0).getGeometry().getLocation().getLat();
                                        double lng = realStateGeocode.getResults().get(0).getGeometry().getLocation().getLng();

                                        MarkerOptions markerOptions = new MarkerOptions();
                                        LatLng latLng = new LatLng(lat,lng);
                                        markerOptions.position(latLng);
                                        mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                });
                            }

                        }
                    });
                }

            }
        });


    }
}