package fr.kosdev.realestatemanager.Controllers.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.kosdev.realestatemanager.Controllers.Activities.ShowAllPhotosActivity;
import fr.kosdev.realestatemanager.Controllers.Activities.UpdatePropertyActivity;
import fr.kosdev.realestatemanager.Controllers.PropertyViewModel;
import fr.kosdev.realestatemanager.Injection.Injection;
import fr.kosdev.realestatemanager.Injection.PropertyViewModelFactory;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;

import static android.content.Intent.getIntent;


public class DetailsFragment extends Fragment {

    @BindView(R.id.detail_img)
    ImageView detailImages;
    @BindView(R.id.detail_type_txt)
    TextView propertyDetailType;
    @BindView(R.id.short_description_txt)
    TextView propertyShortDescription;
    @BindView(R.id.detail_price_txt)
    TextView propertyDetailPrice;
    @BindView(R.id.proximity_interest_point_txt)
    TextView proximityPointOfInterest;
    @BindView(R.id.detail_sale_date_txt)
    TextView detailSaleDate;
    @BindView(R.id.detail_agent_name)
    TextView detailAgentName;
    @BindView(R.id.description_txt) TextView detailPropertyDescription;


    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient mLocationProviderClient;
    private List<String> photoUris;
    private Property mProperty;
    private PropertyViewModel propertyDetailViewModel;
    private List<String> addressList;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        this.configureViewModel();
        this.getPropertyDetails();

        detailImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getActivity().getIntent();
                if (intent != null){
                    if (intent.hasExtra("KEY_DETAIL")){
                        long propertyId = intent.getLongExtra("KEY_DETAIL", 0);
                        Intent photosIntent = new Intent(getActivity(), ShowAllPhotosActivity.class);
                        photosIntent.putExtra("photosKey", propertyId);
                        startActivity(photosIntent);
                    }
                }
                //Intent photosIntent = new Intent(getActivity(), ShowAllPhotosActivity.class);
                //intent.putExtra("photosKey", propertyId);
                //startActivity(photosIntent);

            }
        });
        return view;


    }

    private void configureViewModel(){
        PropertyViewModelFactory propertyViewModelFactory = Injection.providePropertyViewModelFactory(getContext());
        propertyDetailViewModel = new ViewModelProvider(this, propertyViewModelFactory).get(PropertyViewModel.class);
        propertyDetailViewModel.init();
    }
    private void getPropertyDetails(){
        photoUris = new ArrayList<>();
        Intent intent = getActivity().getIntent();
        if (intent != null){
            if (intent.hasExtra("KEY_DETAIL")){
                long propertyId = intent.getLongExtra("KEY_DETAIL",0);
                propertyDetailViewModel.getProperty(propertyId).observe(this, property -> {
                    photoUris.addAll(property.getPhotos());
                    Glide.with(this).load(photoUris.get(0)).into(detailImages);
                    propertyDetailType.setText(property.getType() + "," + property.getStatus());
                    propertyDetailPrice.setText(property.getPrice());
                    propertyShortDescription.setText(property.getAddress() + "," + property.getSurfaceOfProperty());
                    detailSaleDate.setText(property.getDateOfEntry());
                    detailAgentName.setText(property.getRealEstateAgent());
                    detailPropertyDescription.setText(property.getPropertyDescription());
                    proximityPointOfInterest.setText(property.getPointsOfInterest());

                    mProperty = property;
                    configureMap();

                });

            }
        }
        //Property property = (Property) intent.getSerializableExtra("idKey");
        //photoUris.addAll(property.getPhotos());
        //Glide.with(this).load(photoUris.get(0)).into(detailImages);
        //propertyDetailType.setText(property.getType() + "," + property.getStatus());
        //propertyDetailPrice.setText(property.getPrice());
        //propertyShortDescription.setText(property.getAddress() + "," + property.getSurfaceOfProperty());
        //detailSaleDate.setText(property.getDateOfEntry());
        //detailAgentName.setText(property.getRealEstateAgent());
        //detailPropertyDescription.setText(property.getPropertyDescription());
        //proximityPointOfInterest.setText(property.getPointsOfInterest());

    }



    private void configureMap(){
        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            getCurrentLocation();

        }else {

            requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, 20);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 20){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

    //private void updateAddress(List<String> allAddress){
        //addressList.clear();
        //addressList.addAll(allAddress);


    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){

        Task<Location> task = mLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null){
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            mMap = googleMap;
                           // LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            //mMap.addMarker(new MarkerOptions().position(latLng).title("my position"));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            //mMap.setMyLocationEnabled(true);

                            propertyDetailViewModel.getAddressGeocode(mProperty.getAddress()).observe(getViewLifecycleOwner(), realStateGeocode -> {

                                try {
                                    mMap.clear();

                                        double lat = realStateGeocode.getResults().get(0).getGeometry().getLocation().getLat();
                                        double lng = realStateGeocode.getResults().get(0).getGeometry().getLocation().getLng();

                                        MarkerOptions markerOptions = new MarkerOptions();
                                        LatLng geocodeLatLng = new LatLng(lat,lng);
                                        markerOptions.position(geocodeLatLng);
                                        mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(geocodeLatLng));
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(geocodeLatLng, 16));





                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            });
                        }
                    });
                }
            }
        });
    }

    @OnClick(R.id.update_btn)
    public void startUpdateActivity(){
        Intent intent = getActivity().getIntent();
        if (intent != null){
            if (intent.hasExtra("KEY_DETAIL")){
                long propertyId = intent.getLongExtra("KEY_DETAIL", 0);
                Intent updateIntent = new Intent(getActivity(), UpdatePropertyActivity.class);
                updateIntent.putExtra("UPDATE_KEY", propertyId);
                startActivity(updateIntent);
            }
        }

    }

}