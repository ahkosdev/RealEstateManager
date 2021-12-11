package fr.kosdev.realestatemanager.Controllers.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    private List<String> photoUris;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        this.getPropertyDetails();
        this.configureMap();
        return view;
    }
    private void getPropertyDetails(){
        photoUris = new ArrayList<>();
        Intent intent = getActivity().getIntent();
        Property property = (Property) intent.getSerializableExtra("idKey");
        photoUris.add(property.getPhoto());
        Glide.with(this).load(photoUris.get(0)).into(detailImages);
        propertyDetailType.setText(property.getType() + "," + property.getStatus());
        propertyDetailPrice.setText(property.getPrice());
        propertyShortDescription.setText(property.getAddress() + "," + property.getSurfaceOfProperty());
        detailSaleDate.setText(property.getDateOfEntry());
        detailAgentName.setText(property.getRealEstateAgent());
        detailPropertyDescription.setText(property.getPropertyDescription());
        proximityPointOfInterest.setText(property.getPointsOfInterest());

    }

    private void configureMap(){
        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                LatLng latLng = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(latLng).title("my position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }
}