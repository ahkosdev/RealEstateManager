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

    //@BindView(R.id.detail_toolbar)
    //Toolbar detailToolbar;

    private DetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        this.configureAndShowDetailsFragment();
        //this.configureToolbar();
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

    //private void configureToolbar(){
        //setSupportActionBar(detailToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    //}

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.update_menu, menu);
       //return true;
    //}

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.menu.update_menu){
            detailsFragment.startUpdateActivity();
            //this.startUpdateActivity();
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