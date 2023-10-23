package fr.kosdev.realestatemanager.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.kosdev.realestatemanager.Controllers.Fragments.DetailsFragment;
import fr.kosdev.realestatemanager.R;

public class DetailsActivity extends AppCompatActivity {
    private DetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.menu.update_menu){
            detailsFragment.startUpdateActivity();
        }else if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    private void startUpdateActivity(){
        Intent intent = new Intent(this, UpdatePropertyActivity.class);
        startActivity(intent);
    }
}