package fr.kosdev.realestatemanager.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.kosdev.realestatemanager.Controllers.PropertyDetailPhotoAdapter;
import fr.kosdev.realestatemanager.Controllers.PropertyViewModel;
import fr.kosdev.realestatemanager.Injection.Injection;
import fr.kosdev.realestatemanager.Injection.PropertyViewModelFactory;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;

public class ShowAllPhotosActivity extends AppCompatActivity {

    @BindView(R.id.photos_rcv)
    RecyclerView photosRecyclerView;

    PropertyDetailPhotoAdapter mDetailPhotoAdapter;
    private ArrayList<String> photosUris;
    private PropertyViewModel propertyDetailPhotosViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_photos);
        ButterKnife.bind(this);
        this.configureViewModel();
        this.getPropertyDetailPhotos();
        this.configurePhotosRecyclerView();
    }
    private void configurePhotosRecyclerView(){

        photosUris = new ArrayList<>();
        mDetailPhotoAdapter = new PropertyDetailPhotoAdapter(photosUris);
        photosRecyclerView.setAdapter(mDetailPhotoAdapter);


    }
    private void configureViewModel(){
        PropertyViewModelFactory propertyViewModelFactory = Injection.providePropertyViewModelFactory(getApplicationContext());
        propertyDetailPhotosViewModel = new ViewModelProvider(this, propertyViewModelFactory).get(PropertyViewModel.class);
        propertyDetailPhotosViewModel.init();
    }

    private void getPropertyDetailPhotos(){

        photosUris = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("photosKey")){
                long propertyDetailId = intent.getLongExtra("photosKey", 0);
                propertyDetailPhotosViewModel.getProperty(propertyDetailId).observe(this, property -> {
                    photosUris.addAll(property.getPhotos());
                    mDetailPhotoAdapter.notifyDataSetChanged();

                });
            }

        }
    }
}