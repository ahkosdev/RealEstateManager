package fr.kosdev.realestatemanager.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.kosdev.realestatemanager.Controllers.Fragments.DetailsFragment;
import fr.kosdev.realestatemanager.Controllers.Fragments.HomePageFragment;
import fr.kosdev.realestatemanager.Controllers.Fragments.MapsFragment;
import fr.kosdev.realestatemanager.Controllers.PropertyViewHolderAdapter;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_lyt)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.homepage_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_nav_view)
    NavigationView mNavigationView;

    private HomePageFragment homePageFragment;
    private DetailsFragment detailsFragment;
    private Fragment mapFragment;
    private static final int MAP_FRAGMENT = 1;
    PropertyViewHolderAdapter propertyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);

        this.configureNavigationView();
        this.configureAndShowHomePageFragment();
        this.configureAndShowDetailsFragment();
        this.configureToolbar();
        this.configureDrawerLayout();


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
                this.startMapActivity();
                break;

            case R.id.property_ad_menu:
                this.startAddPropertyActivity();
                break;

            case R.id.loan_menu:
                this.startLoanSimulatorActivity();
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

    private void startMapActivity(){
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
    }

    private void startTransactionFragment(Fragment fragment){
            getSupportFragmentManager().beginTransaction().replace(R.id.drawer_frame_layout,fragment).commit();

    }

    private void showMapFragment(){
        if (this.mapFragment == null) this.mapFragment = MapsFragment.newInstance();
        this.startTransactionFragment(mapFragment);
    }
    private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case MAP_FRAGMENT:
                this.showMapFragment();
                break;
            default:
                break;

        }
    }

    private void startLoanSimulatorActivity(){
        Intent loanIntent = new Intent(this, LoanSimulatorActivity.class);
        startActivity(loanIntent);

    }
}