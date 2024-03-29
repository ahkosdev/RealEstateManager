package fr.kosdev.realestatemanager.Controllers.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import fr.kosdev.realestatemanager.Controllers.Activities.DetailsActivity;
import fr.kosdev.realestatemanager.Controllers.PropertyViewModel;
import fr.kosdev.realestatemanager.Injection.Injection;
import fr.kosdev.realestatemanager.Injection.PropertyViewModelFactory;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;


public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private FusedLocationProviderClient mProviderClient;
    private PropertyViewModel mPropertyViewModel;
    private List<Property> mProperties;

    public static MapsFragment newInstance(){
        return (new MapsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, view);

        this.configureMapViewModel();
        //this.getMapProperties();
        this.configurePropertyMap();
        return view;

    }

    private void configureMapViewModel(){
        PropertyViewModelFactory propertyViewModelFactory = Injection.providePropertyViewModelFactory(getContext());
        mPropertyViewModel = new ViewModelProvider(this,propertyViewModelFactory).get(PropertyViewModel.class);
        mPropertyViewModel.init();
    }

    private void configurePropertyMap(){

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        mProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            getPropertiesNearbyAgentLocation();

        }else {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 40);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 40){
            if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getPropertiesNearbyAgentLocation();
            }
        }
    }

    private void getMapProperties(){
        mPropertyViewModel.getProperties().observe(getViewLifecycleOwner(), this::updateMapProperties);
    }

    private void updateMapProperties(List<Property> properties){
        mProperties = new ArrayList<>();
        mProperties.addAll(properties);
    }

    private void getPropertiesNearbyAgentLocation(){

        mPropertyViewModel.getProperties().observe(getViewLifecycleOwner(), properties -> {

            mProperties = new ArrayList<>();
            mProperties.addAll(properties);
            Task<Location> task = mProviderClient.getLastLocation();
            task.addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        mMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                mMap = googleMap;
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                mMap.setMyLocationEnabled(true);

                                for (int i = 0 ; i <mProperties.size(); i++){

                                    String propertyAddress = mProperties.get(i).getAddress();
                                    long propertyId = mProperties.get(i).getId();
                                    mPropertyViewModel.getAddressGeocode(mProperties.get(i).getAddress()).observe(getViewLifecycleOwner(), realStateGeocode -> {

                                        try {
                                            //mMap.clear();

                                            double lat = realStateGeocode.getResults().get(0).getGeometry().getLocation().getLat();
                                            double lng = realStateGeocode.getResults().get(0).getGeometry().getLocation().getLng();
                                            //String placeId = realStateGeocode.getResults().get(0).getPlaceId();
                                            LatLng propertylatLng = new LatLng(lat,lng);
                                            MarkerOptions markerOptions = new MarkerOptions();
                                            markerOptions.position(propertylatLng);
                                            markerOptions.title(propertyAddress);
                                            mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))).setTag(propertyId);
                                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                @Override
                                                public void onInfoWindowClick(@NonNull Marker marker) {
                                                    long propertyId = (long) marker.getTag();
                                                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                                                    intent.putExtra("MAP_KEY_DETAIL", propertyId);
                                                    startActivity(intent);

                                                }
                                            });

                                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                @Override
                                                public boolean onMarkerClick(@NonNull Marker marker) {

                                                    long propertyId = (long) marker.getTag();
                                                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                                                    intent.putExtra("MAP_KEY_DETAIL", propertyId);
                                                    startActivity(intent);
                                                    return false;
                                                }
                                            });
                                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                            //mMap.setMyLocationEnabled(true);


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
        });



    }
}