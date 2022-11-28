package fr.kosdev.realestatemanager.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

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
import fr.kosdev.realestatemanager.Controllers.Fragments.MapsFragment;
import fr.kosdev.realestatemanager.Controllers.PropertyViewModel;
import fr.kosdev.realestatemanager.Injection.Injection;
import fr.kosdev.realestatemanager.Injection.PropertyViewModelFactory;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;

public class MapsActivity extends AppCompatActivity {

    private Fragment mapsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.configureAndShowMapFragment();

    }

    private void configureAndShowMapFragment(){
        mapsFragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.property_map,mapsFragment).commit();
    }

}