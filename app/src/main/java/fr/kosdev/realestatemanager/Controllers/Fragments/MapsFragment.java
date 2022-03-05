package fr.kosdev.realestatemanager.Controllers.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import butterknife.ButterKnife;
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
    private List<Property> mProperties = HomePageFragment.mPropertyList;

    public static MapsFragment newInstance(){
        return (new MapsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, view);
        this.configurePropertyMap();
        this.configureMapViewModel();
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

    private void getPropertiesNearbyAgentLocation(){

        Task<Location> task = mProviderClient.getLastLocation();
        task.addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    mMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            mMap = googleMap;

                            for (int i = 0 ; mProperties.size() < i; i++){

                                mPropertyViewModel.getAddressGeocode(mProperties.get(i).getAddress()).observe(getViewLifecycleOwner(), realStateGeocode -> {

                                    try {
                                        mMap.clear();

                                        double lat = realStateGeocode.getResults().get(0).getGeometry().getLocation().getLat();
                                        double lng = realStateGeocode.getResults().get(0).getGeometry().getLocation().getLng();
                                        LatLng latLng = new LatLng(lat,lng);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(latLng);
                                        mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                        mMap.setMyLocationEnabled(true);

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