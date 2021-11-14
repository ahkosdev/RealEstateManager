package fr.kosdev.realestatemanager.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fr.kosdev.realestatemanager.Controllers.Fragments.DetailsFragment;
import fr.kosdev.realestatemanager.Controllers.Fragments.HomePageFragment;
import fr.kosdev.realestatemanager.Controllers.PropertyViewHolderAdapter;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;

public class HomePageActivity extends AppCompatActivity {

    @BindView(R.id.property_rcv)
    RecyclerView propertyRecyclerView;

    private HomePageFragment homePageFragment;
    private DetailsFragment detailsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        this.configureAndShowHomePageFragment();
        this.configureAndShowDetailsFragment();
    }

    private void configureAndShowHomePageFragment(){

        homePageFragment = (HomePageFragment) getSupportFragmentManager().findFragmentById(R.id.homepage_frame_layout);
        if (homePageFragment==null){
            homePageFragment = new HomePageFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.homepage_frame_layout, homePageFragment)
                    .commit();
        }
    }

    private void configureAndShowDetailsFragment(){

        detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_frame_layout);
        if (detailsFragment == null && findViewById(R.id.details_frame_layout) != null){
            detailsFragment = new DetailsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_frame_layout,detailsFragment)
                    .commit();
        }
    }


}