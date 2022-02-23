package fr.kosdev.realestatemanager.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fr.kosdev.realestatemanager.Controllers.Fragments.DetailsFragment;
import fr.kosdev.realestatemanager.Controllers.Fragments.HomePageFragment;
import fr.kosdev.realestatemanager.Controllers.PropertyViewHolderAdapter;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.property_rcv)
    RecyclerView propertyRecyclerView;
    @BindView(R.id.drawer_lyt)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.homepage_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_nav_view)
    NavigationView mNavigationView;

    private HomePageFragment homePageFragment;
    private DetailsFragment detailsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        this.configureAndShowHomePageFragment();
        this.configureAndShowDetailsFragment();
        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
    }

    private void configureAndShowHomePageFragment(){

        homePageFragment = (HomePageFragment) getSupportFragmentManager().findFragmentById(R.id.drawer_frame_layout);
        if (homePageFragment==null){
            homePageFragment = new HomePageFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.drawer_frame_layout, homePageFragment)
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

    private void configureToolbar(){
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.drawer_map_menu:
                break;

            case R.id.property_ad_menu:
                this.startAddPropertyActivity();
                break;

            default:
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }


    private void configureNavigationView(){
        mNavigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private void configureDrawerLayout(){

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void startAddPropertyActivity(){
        Intent intent = new Intent(this, AddPropertyActivity.class);
        startActivity(intent);
    }
}