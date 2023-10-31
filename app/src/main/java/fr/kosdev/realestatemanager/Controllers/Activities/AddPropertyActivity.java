package fr.kosdev.realestatemanager.Controllers.Activities;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.kosdev.realestatemanager.Controllers.PropertyImageAdapter;
import fr.kosdev.realestatemanager.Controllers.PropertyViewModel;
import fr.kosdev.realestatemanager.Injection.Injection;
import fr.kosdev.realestatemanager.Injection.PropertyViewModelFactory;
import fr.kosdev.realestatemanager.Models.Property;
import fr.kosdev.realestatemanager.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class AddPropertyActivity extends AppCompatActivity {

    @BindView(R.id.property_type_atc)
    AutoCompleteTextView propertyTypesAutocomplete;
    @BindView(R.id.property_price_txt)
    TextInputEditText priceOfProperty;
    @BindView(R.id.property_area_txt)TextInputEditText surfaceOfProperty;
    @BindView(R.id.rooms_number_txt) TextInputEditText numberOfRooms;
    @BindView(R.id.property_address_txt) TextInputEditText propertyAddress;
    @BindView(R.id.sale_date_picker) TextInputEditText saleDate;
    @BindView(R.id.sold_date_picker) TextInputEditText dateOfSale;
    @BindView(R.id.available_chip)
    Chip availableStatus;
    @BindView(R.id.sold_chip) Chip soldStatus;
    @BindView(R.id.agent_name_txt) TextInputEditText agentName;
    @BindView(R.id.property_description_txt) TextInputEditText propertyDescription;
    @BindView(R.id.save_add_btn)
    MaterialButton saveButton;
    @BindView(R.id.property_photo_rcv)
    RecyclerView propertyImagesRcv;
    @BindView(R.id.add_photo_fab)
    FloatingActionButton addPhotoFab;

    ArrayAdapter<String> propertyTypeAdapter;
    ArrayList<String> selections = new ArrayList<>();
    PropertyViewModel propertyViewModel;
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    private Uri uriImageSelected;
     ArrayList<String> imagesUriList;
    private int position = 0;
    PropertyImageAdapter imageAdapter;
    private final int NOTIFICATION_ID = 001;
    private final String NOTIFICATION_TAG = "RealStateManager";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        ButterKnife.bind(this);
        configureTypeSpinner();
        selectSaleDate();
        selectDateOfSale();
        configureImageRecyclerView();
        configureViewModel();
        addProperty();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CHOOSE_PHOTO){
            if (resultCode == RESULT_OK){
                if (data.getClipData() != null){
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i< count; i++){
                        this.uriImageSelected = data.getClipData().getItemAt(i).getUri();
                        imagesUriList.add(uriImageSelected.toString());
                    }
                    imageAdapter.notifyDataSetChanged();

            }else if (data.getData() != null) {
                    this.uriImageSelected = data.getData();
                    imagesUriList.add(uriImageSelected.toString());
                    imageAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(this, getString(R.string.no_image_choose_text), Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

    @OnClick(R.id.add_photo_fab)
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void addImages(){
        this.chooseImageFromPhone();
    }

    private void chooseImageFromPhone(){
        if (!EasyPermissions.hasPermissions(this, PERMS)){
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_file_access),RC_IMAGE_PERMS, PERMS);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "SELECT PHOTOS"),RC_CHOOSE_PHOTO);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data){

    }
    private void configureImageRecyclerView(){
        imagesUriList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imageAdapter = new PropertyImageAdapter(imagesUriList);
        propertyImagesRcv.setAdapter(imageAdapter);
        propertyImagesRcv.setLayoutManager(layoutManager);

    }

    private void configureViewModel(){
        PropertyViewModelFactory propertyViewModelFactory = Injection.providePropertyViewModelFactory(this);
        propertyViewModel = new ViewModelProvider(this,propertyViewModelFactory).get(PropertyViewModel.class);
    }

    private void configureTypeSpinner (){
        String [] propertyTypes = getResources().getStringArray(R.array.type_of_property);
        propertyTypeAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,propertyTypes);
        propertyTypesAutocomplete.setAdapter(propertyTypeAdapter);
        propertyTypesAutocomplete.setThreshold(1);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void selectSaleDate(){
        saleDate.setInputType(InputType.TYPE_NULL);
        saleDate.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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
        builder.setTitleText("Select A Date");
        MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                saleDate.setText(materialDatePicker.getHeaderText());
            }
        });
    }
    @SuppressLint("ClickableViewAccessibility")
    private void selectDateOfSale(){
        dateOfSale.setInputType(InputType.TYPE_NULL);
        dateOfSale.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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
        builder.setTitleText("Select A Date");
        MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dateOfSale.setText(materialDatePicker.getHeaderText());
            }
        });
    }



    public void selectItem(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){

            case R.id.school_box:
                if (checked){
                    selections.add(getString(R.string.school));
                }else {
                    selections.remove(getString(R.string.school));
                }
                break;

            case R.id.high_school_box:
                if (checked){
                    selections.add(getString(R.string.high_school));
                }else {
                    selections.remove(getString(R.string.high_school));
                }
                break;

            case R.id.university_box:
                if (checked){
                    selections.add(getString(R.string.university));
                }else {
                    selections.remove(getString(R.string.university));
                }
                break;

            case R.id.mall_box:
                if (checked){
                    selections.add(getString(R.string.mall));
                }else {
                    selections.remove(getString(R.string.mall));
                }
                break;

            case R.id.hospital_box:
                if (checked){
                    selections.add(getString(R.string.hospital));
                }else {
                    selections.remove(getString(R.string.hospital));
                }
                break;

            case R.id.restaurant_box:
                if (checked){
                    selections.add(getString(R.string.restaurant));
                }else {
                    selections.remove(getString(R.string.restaurant));
                }
                break;

            case R.id.park_box:
                if (checked){
                    selections.add(getString(R.string.park));
                }else {
                    selections.remove(getString(R.string.park));
                }
                break;

            case R.id.museum_box:
                if (checked){
                    selections.add(getString(R.string.museum));
                }else {
                    selections.remove(getString(R.string.museum));
                }
                break;

            case R.id.stadium_box:
                if (checked){
                    selections.add(getString(R.string.stadium));
                }else {
                    selections.remove(getString(R.string.stadium));
                }
                break;



        }
    }
    private void addProperty(){

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  final_userSelection = "";
                for (String selection : selections){
                    final_userSelection = final_userSelection + selection + ",";

                }
                Date date = new Date();
                Date dateOut = new Date();
                DateFormat df = new SimpleDateFormat("d MMMM yyyy");
                try {
                    date = df.parse(saleDate.getText().toString());
                }catch (ParseException e){
                    e.printStackTrace();
                }

                try {
                    dateOut = df.parse(dateOfSale.getText().toString());
                }catch (ParseException e){
                    e.printStackTrace();
                }
                long id = (long) (Math.random()*50000);
                if (priceOfProperty.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_price),Toast.LENGTH_SHORT).show();
                } else if (numberOfRooms.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_room), Toast.LENGTH_SHORT).show();
                }else if (surfaceOfProperty.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_surface), Toast.LENGTH_SHORT).show();
                }else if (imagesUriList.size() == 0){
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_photo), Toast.LENGTH_SHORT).show();
                }
                else {
                    Property property = new Property(
                            imagesUriList,
                            propertyTypesAutocomplete.getText().toString(),
                            Integer.parseInt(priceOfProperty.getText().toString()),
                            Integer.parseInt(numberOfRooms.getText().toString()),
                            Integer.parseInt(surfaceOfProperty.getText().toString()),
                            propertyDescription.getText().toString(),
                            propertyAddress.getText().toString(),
                            final_userSelection,
                            availableStatus.getText().toString(),
                            date,
                            dateOut,
                            agentName.getText().toString()

                    );
                    propertyViewModel.createProperty(property);

                    Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

                    String channelId = "default_channel";
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(view.getContext(), channelId)
                            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                            .setContentTitle(property.getAddress())
                            .setContentText(getString(R.string.notification_text))
                            .setLargeIcon(BitmapFactory.decodeFile("property.getPhotos()"))
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                        CharSequence channelName = "Success Notifications";
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
                        notificationManager.createNotificationChannel(channel);
                    }
                    notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());

                    finish();
                }

            }
        });
    }

    private void sendVisualNotification(){

        Intent intent = new Intent(this, DetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        String channelId = "default_channel";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Notification")
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            CharSequence channelName = "Success Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());


    }


}