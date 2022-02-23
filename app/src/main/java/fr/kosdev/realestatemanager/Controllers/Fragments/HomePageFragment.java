package fr.kosdev.realestatemanager.Controllers.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.kosdev.realestatemanager.Controllers.Activities.AddPropertyActivity;
import fr.kosdev.realestatemanager.Controllers.PropertyViewHolder;
import fr.kosdev.realestatemanager.Controllers.PropertyViewHolderAdapter;
import fr.kosdev.realestatemanager.Controllers.PropertyViewModel;
import fr.kosdev.realestatemanager.Injection.Injection;
import fr.kosdev.realestatemanager.Injection.PropertyViewModelFactory;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;


public class HomePageFragment extends Fragment {


    @BindView(R.id.property_rcv)
    RecyclerView propertyRecyclerView;
    @BindView(R.id.add_fab)
    FloatingActionButton addFab;

    private PropertyViewHolderAdapter mPropertyViewHolderAdapter;
    public static List<Property> mPropertyList;
    PropertyViewModel mPropertyViewModel;


    public HomePageFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        ButterKnife.bind(this,view);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddPropertyActivity.class);
                startActivity(intent);

            }
        });

        this.configureRecyclerView();
        this.configureViewModel();
        this.getProperties();
        return view;
    }
    private void configureRecyclerView(){

        mPropertyList = new ArrayList<>();
        mPropertyViewHolderAdapter = new PropertyViewHolderAdapter(mPropertyList);
        propertyRecyclerView.setAdapter(mPropertyViewHolderAdapter);
        mPropertyViewHolderAdapter.notifyDataSetChanged();
    }
    private void configureViewModel(){

        PropertyViewModelFactory propertyViewModelFactory = Injection.providePropertyViewModelFactory(getContext());
        mPropertyViewModel = new ViewModelProvider(this,propertyViewModelFactory).get(PropertyViewModel.class);
        mPropertyViewModel.init();
    }

    private void getProperties(){
        mPropertyViewModel.getProperties().observe(getViewLifecycleOwner(), this::updateProperties);
    }

    private void updateProperties(List<Property> properties){
        mPropertyList.clear();
        mPropertyList.addAll(properties);
        mPropertyViewHolderAdapter.notifyDataSetChanged();
    }

}