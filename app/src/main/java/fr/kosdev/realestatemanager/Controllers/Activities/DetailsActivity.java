package fr.kosdev.realestatemanager.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import fr.kosdev.realestatemanager.Controllers.Fragments.DetailsFragment;
import fr.kosdev.realestatemanager.R;

public class DetailsActivity extends AppCompatActivity {

    private DetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        this.configureAndShowDetailsFragment();
    }

    private void configureAndShowDetailsFragment(){

        detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_frame_layout);

        if (detailsFragment== null){
            detailsFragment = new DetailsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_frame_layout, detailsFragment)
                    .commit();
        }
    }
}