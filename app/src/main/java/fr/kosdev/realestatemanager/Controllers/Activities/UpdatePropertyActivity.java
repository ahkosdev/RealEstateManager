package fr.kosdev.realestatemanager.Controllers.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.kosdev.realestatemanager.Controllers.PropertyImageAdapter;
import fr.kosdev.realestatemanager.Controllers.PropertyViewModel;
import fr.kosdev.realestatemanager.Controllers.UpdateImageAdapter;
import fr.kosdev.realestatemanager.Injection.Injection;
import fr.kosdev.realestatemanager.Injection.PropertyViewModelFactory;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class UpdatePropertyActivity extends AppCompatActivity implements View.OnLongClickListener {

    @BindView(R.id.property_type_atc_up)
    AutoCompleteTextView updatePropertyTypesAutocomplete;
    @BindView(R.id.property_price_txt_up)
    TextInputEditText updatePriceOfProperty;
    @BindView(R.id.property_area_txt_up)
    TextInputEditText updateSurfaceOfProperty;
    @BindView(R.id.rooms_number_txt_up)
    TextInputEditText updateNumberOfRooms;
    @BindView(R.id.property_address_txt_up)
    TextInputEditText updatePropertyAddress;
    @BindView(R.id.sale_date_picker_up)
    TextInputEditText updateSaleDate;
    @BindView(R.id.sold_date_picker_up)
    TextInputEditText updateDateOfSale;
    @BindView(R.id.available_chip_up)
    Chip availableStatus;
    @BindView(R.id.sold_chip_up)
    Chip soldStatus;
    @BindView(R.id.agent_name_txt_up)
    TextInputEditText updateAgentName;
    @BindView(R.id.property_description_txt_up)
    TextInputEditText updatePropertyDescription;
    @BindView(R.id.save_update_btn)
    MaterialButton updateButton;
    @BindView(R.id.property_photo_rcv_up)
    RecyclerView propertyPhotosRcv;
    @BindView(R.id.update_tlb)
    Toolbar updateToolbar;
    @BindView(R.id.image_selected_counter)
    TextView imageSelectedCounter;

    ArrayAdapter<String> propertyTypeAdapter;
    ArrayList<String> selections = new ArrayList<>();
    PropertyViewModel mPropertyViewModel;
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 10;
    private static final int RC_CHOOSE_PHOTO = 20;
    private Uri imageSelectedUris;
    ArrayList<String> selectedImagesList;
    ArrayList<String> deletedImagesList = new ArrayList<>();
    UpdateImageAdapter mImageAdapter;
    public boolean isContexualModeEnable = false;
    int counter = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_property);
        ButterKnife.bind(this);
        this.configureTypeSpinner();
        this.selectSaleDate();
        this.selectDateOfSale();
        configureUpdateRecyclerView();
        this.configureViewModel();
        this.showProperty();
        this.configureToolbar();
        //delectedImagesList = new ArrayList<>();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_CHOOSE_PHOTO){
            if (resultCode == RESULT_OK){
                if (data.getClipData() != null){
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i< count; i++){
                        this.imageSelectedUris = data.getClipData().getItemAt(i).getUri();
                        selectedImagesList.add(imageSelectedUris.toString());
                    }
                    mImageAdapter.notifyDataSetChanged();
                }
            }
            else if (data.getData() != null){
                imageSelectedUris = data.getData();
                selectedImagesList.add(imageSelectedUris.toString());
                mImageAdapter.notifyDataSetChanged();
            }
            Toast.makeText(this, getString(R.string.no_image_choose_text), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.add_photo_btn_up)
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void addPhoto(){
        this.chooseImageFromPhone();
    }

    private void chooseImageFromPhone(){
        if (!EasyPermissions.hasPermissions(this, PERMS)){
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_file_access),RC_IMAGE_PERMS,PERMS);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "SELECT PHOTOS"), RC_CHOOSE_PHOTO);
    }

    private void configureToolbar(){
        setSupportActionBar(updateToolbar);
        imageSelectedCounter.setText("Update Property");

    }

    private void configureUpdateRecyclerView(){
        selectedImagesList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mImageAdapter = new UpdateImageAdapter(selectedImagesList, UpdatePropertyActivity.this);
        propertyPhotosRcv.setAdapter(mImageAdapter);
        propertyPhotosRcv.setLayoutManager(layoutManager);
    }

    private void configureViewModel(){
        PropertyViewModelFactory propertyViewModelFactory = Injection.providePropertyViewModelFactory(this);
        mPropertyViewModel = new ViewModelProvider(this,propertyViewModelFactory).get(PropertyViewModel.class);
        mPropertyViewModel.init();
    }

    private void configureTypeSpinner(){
        String [] propertyTypes = getResources().getStringArray(R.array.type_of_property);
        propertyTypeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item,propertyTypes);
        updatePropertyTypesAutocomplete.setAdapter(propertyTypeAdapter);
        updatePropertyTypesAutocomplete.setThreshold(1);
    }

    private void selectSaleDate(){
        updateSaleDate.setInputType(InputType.TYPE_NULL);
        updateSaleDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    configureSaleDatePicker();
                }
                return false;
            }
        });
    }
    private void configureSaleDatePicker(){
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                updateSaleDate.setText(materialDatePicker.getHeaderText());
            }
        });

    }

    private void selectDateOfSale(){
        updateDateOfSale.setInputType(InputType.TYPE_NULL);
        updateDateOfSale.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    configureDateOfSalePicker();
                }
                return false;
            }
        });
    }

    private void configureDateOfSalePicker(){
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                updateDateOfSale.setText(materialDatePicker.getHeaderText());
            }
        });
    }

    public void selectItem(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){

            case R.id.school_box_up:
                if (checked){
                    selections.add(getString(R.string.school));
                }else {
                    selections.remove(getString(R.string.school));
                }
                break;

            case R.id.high_school_box_up:
                if (checked){
                    selections.add(getString(R.string.high_school));
                }else {
                    selections.remove(R.string.high_school);
                }
                break;

            case R.id.university_box_up:
                if (checked){
                    selections.add(getString(R.string.university));
                }else {
                    selections.remove(getString(R.string.university));
                }
                break;

            case R.id.mall_box_up:
                if (checked){
                    selections.add(getString(R.string.mall));
                }else {
                    selections.remove(getString(R.string.mall));
                }
                break;

            case R.id.hospital_box_up:
                if (checked){
                    selections.add(getString(R.string.hospital));
                }else {
                    selections.remove(getString(R.string.hospital));
                }
                break;

            case R.id.restaurant_box_up:
                if (checked){
                    selections.add(getString(R.string.restaurant));
                }else {
                    selections.remove(getString(R.string.restaurant));
                }
                break;

            case R.id.park_box_up:
                if (checked){
                    selections.add(getString(R.string.park));
                }else {
                    selections.remove(getString(R.string.park));
                }
                break;

            case R.id.museum_box_up:
                if (checked){
                    selections.add(getString(R.string.museum));
                }else {
                    selections.remove(getString(R.string.museum));
                }
                break;

            case R.id.stadium_box_up:
                if (checked){
                    selections.add(getString(R.string.stadium));
                }else {
                    selections.remove(getString(R.string.stadium));
                }
                break;

            default:
                break;


        }
    }

    private void showProperty(){
        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("UPDATE_KEY")){
                long propertyId = intent.getLongExtra("UPDATE_KEY", 0);
                mPropertyViewModel.getProperty(propertyId).observe(this, property -> {

                    selectedImagesList.addAll(property.getPhotos());
                    mImageAdapter.notifyDataSetChanged();
                    updatePropertyTypesAutocomplete.setText(property.getType());
                    updatePriceOfProperty.setText(Integer.toString(property.getPrice()));
                    updateSurfaceOfProperty.setText(Integer.toString(property.getSurfaceOfProperty()));
                    updateNumberOfRooms.setText(Integer.toString(property.getNumberOfRooms()));
                    updatePropertyAddress.setText(property.getAddress());
                    //updateSaleDate.setText(property.getDateOfEntry());
                    updatePropertyDescription.setText(property.getPropertyDescription());
                    updateAgentName.setText(property.getRealEstateAgent());
                    availableStatus.setText(property.getStatus());

                });
            }
        }
    }
    @OnClick(R.id.save_update_btn)
    public void updateProperty(){
        String final_userSelection = "";
        for (String selection : selections){
            final_userSelection = final_userSelection + selection + ",";
        }

        Date dateIn = new Date();
        Date dateOut = new Date();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try {
            dateIn = df.parse(updateSaleDate.getText().toString());
        }catch (ParseException e){
            e.printStackTrace();
        }
        try {
            dateOut = df.parse(updateDateOfSale.getText().toString());
        }catch (ParseException e){
            e.printStackTrace();
        }

        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("UPDATE_KEY")){
                long propertyId = intent.getLongExtra("UPDATE_KEY", 0);

                Property updateProperty = new Property(
                        propertyId,
                        selectedImagesList,
                        updatePropertyTypesAutocomplete.getText().toString(),
                        Integer.parseInt(updatePriceOfProperty.getText().toString()),
                        Integer.parseInt(updateNumberOfRooms.getText().toString()),
                        Integer.parseInt(updateSurfaceOfProperty.getText().toString()),
                        updatePropertyDescription.getText().toString(),
                        updatePropertyAddress.getText().toString(),
                        final_userSelection,
                        soldStatus.getText().toString(),
                        dateIn,
                        dateOut,
                        //updateSaleDate.getText().toString(),
                        //updateDateOfSale.getText().toString(),
                        updateAgentName.getText().toString()
                );
                mPropertyViewModel.updateProperty(updateProperty);
                finish();
            }
        }

    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.update_menu, menu);
        //return true;
    //}

    @Override
    public boolean onLongClick(View view) {
        isContexualModeEnable = true;
        //updateToolbar.getMenu().clear();
        updateToolbar.inflateMenu(R.menu.delete_image_menu);
        getSupportActionBar().setTitle("0 Image Selected");
        updateToolbar.setBackgroundColor(getColor(R.color.colorAccent));
        mImageAdapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    public void makeSelection(View view, int adapterPosition){
        if (((CheckBox)view).isChecked()){
            deletedImagesList.add(selectedImagesList.get(adapterPosition));
            counter++;
            updateCounter();

        }else {
            deletedImagesList.remove(selectedImagesList.get(adapterPosition));
            counter--;
            updateCounter();
        }
    }

    public void updateCounter(){
        imageSelectedCounter.setText(counter + "Item Selected");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== R.id.delete_menu){
            mImageAdapter.removeImage(deletedImagesList);
            removeContextualActionMode();
        }else if (item.getItemId()== android.R.id.home){
            removeContextualActionMode();
            mImageAdapter.notifyDataSetChanged();
        }
        return true;
    }

    private void removeContextualActionMode(){
        isContexualModeEnable = false;
        imageSelectedCounter.setText("Update Property");
        updateToolbar.getMenu().clear();
        //updateToolbar.inflateMenu(R.menu.update_menu);
        counter = 0;
        deletedImagesList.clear();
        mImageAdapter.notifyDataSetChanged();
        updateToolbar.setBackgroundColor(getColor(R.color.colorPrimary));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}