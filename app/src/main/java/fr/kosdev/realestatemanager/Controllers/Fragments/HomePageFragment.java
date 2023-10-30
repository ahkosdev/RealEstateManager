package fr.kosdev.realestatemanager.Controllers.Fragments;

import android.annotation.SuppressLint;
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
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.kosdev.realestatemanager.Controllers.Activities.AddPropertyActivity;
import fr.kosdev.realestatemanager.Controllers.PropertyViewHolder;
import fr.kosdev.realestatemanager.Controllers.PropertyViewHolderAdapter;
import fr.kosdev.realestatemanager.Controllers.PropertyViewModel;
import fr.kosdev.realestatemanager.Database.PropertySimpleSqliteQuery;
import fr.kosdev.realestatemanager.Injection.Injection;
import fr.kosdev.realestatemanager.Injection.PropertyViewModelFactory;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;


public class HomePageFragment extends Fragment {


    @BindView(R.id.property_rcv)
    RecyclerView propertyRecyclerView;
    @BindView(R.id.minPrice)
    TextInputEditText searchMinPrice;
    @BindView(R.id.maxPrice)
    TextInputEditText searchMaxPrice;
    @BindView(R.id.search_btn)
    MaterialButton searchButton;
    @BindView(R.id.rooms_number)
    TextInputEditText roomsNumber;
    @BindView(R.id.minSurface)
    TextInputEditText minSurface;
    @BindView(R.id.maxSurface)
    TextInputEditText maxSurface;
    @BindView(R.id.search_type_atc)
    AutoCompleteTextView searchType;
    @BindView(R.id.search_address_txt)
    TextInputEditText searchAddress;
    @BindView(R.id.search_date_txt)
    TextInputEditText searchDate;
   @BindView(R.id.search_layout)
    LinearLayout searchLayout;
   @BindView(R.id.add_fab)
   FloatingActionButton addFab;

    private PropertyViewHolderAdapter mPropertyViewHolderAdapter;
    private List<Property> mPropertyList;
    PropertyViewModel mPropertyViewModel;
    private String minPrice;
    private String maxPrice;
    private String query;
    private List<Property> searchWithPriceList;
    PropertySimpleSqliteQuery mSqliteQuery;
    SimpleSQLiteQuery mQuery;
    List<Object> conditions;
    ArrayAdapter<String> searchPropertyTypeAdapter;


    public HomePageFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        ButterKnife.bind(this,view);


        this.configureRecyclerView();
        this.configureViewModel();
        this.getProperties();
        this.configureSearchPriceSpinner();
        this.selectSearchedDate();
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

    private void configureSearchPriceSpinner(){
        String [] propertiesTypes = getResources().getStringArray(R.array.type_of_property);
        searchPropertyTypeAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, propertiesTypes);
        searchType.setAdapter(searchPropertyTypeAdapter);
        searchType.setThreshold(1);

    }
    @SuppressLint("ClickableViewAccessibility")
    private void selectSearchedDate(){
        searchDate.setInputType(InputType.TYPE_NULL);
        searchDate.setOnTouchListener(new View.OnTouchListener() {
           // @SuppressLint(ClickableViewAccessibility)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    configureSearchedDatePicker();
                }
                return false;
            }
        });
    }
    private void configureSearchedDatePicker(){
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getChildFragmentManager(), "DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                searchDate.setText(materialDatePicker.getHeaderText());

            }
        });

    }

    private void getPropertiesWithPrice(){
        mSqliteQuery = new PropertySimpleSqliteQuery();
        mQuery = mSqliteQuery.simpleSqliteQuery(searchMinPrice.getText().toString(), searchMaxPrice.getText().toString(),
                roomsNumber.getText().toString(), minSurface.getText().toString(), maxSurface.getText().toString(), searchType.getText().toString(),
                searchDate.getText().toString());
        mPropertyViewModel.getPropertiesWithFilter(mQuery).observe(getViewLifecycleOwner(), this::updateWithPriceSearch);

    }

    private void updateWithPriceSearch(List<Property> properties){
        mPropertyList.clear();
        mPropertyList.addAll(properties);
        mPropertyViewHolderAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.search_btn)
    public void searchPropertiesWithPrice() {
        this.getPropertiesWithPrice();
        if (searchLayout.getVisibility() == View.VISIBLE) {
            searchLayout.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.search_address_txt)
    public void showFilter() {
            if (searchLayout.getVisibility() == View.GONE) {
                searchLayout.setVisibility(View.VISIBLE);
            }else {
                searchLayout.setVisibility(View.GONE);
            }
        }

    @OnClick(R.id.add_fab)
    public void startAddActivity(){
        Intent intent = new Intent(getActivity(), AddPropertyActivity.class);
        startActivity(intent);
    }



}