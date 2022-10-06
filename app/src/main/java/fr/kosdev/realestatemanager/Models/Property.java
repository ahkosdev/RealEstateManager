package fr.kosdev.realestatemanager.Models;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.kosdev.realestatemanager.Utils.DateConverter;

@Entity
public class Property implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String type;
    private int price;
    private int numberOfRooms;
    private int surfaceOfProperty;
    private String propertyDescription;
    //private String photo;
     ArrayList<String> photos;
    private String address;
    private String city;
    private String pointsOfInterest;
    private String status;
    @TypeConverters(DateConverter.class)
    private Date dateOfEntry;
    @TypeConverters(DateConverter.class)
    private Date dateOfSale;
    private String realEstateAgent;

    public Property(long id, ArrayList<String> photos, String type, int price, int numberOfRooms, int surfaceOfProperty, String propertyDescription, String address, String city, String pointsOfInterest, String status, Date dateOfEntry, Date dateOfSale, String realEstateAgent) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.numberOfRooms = numberOfRooms;
        this.surfaceOfProperty = surfaceOfProperty;
        this.propertyDescription = propertyDescription;
        this.photos = photos;
        this.address = address;
        this.city = city;
        this.pointsOfInterest = pointsOfInterest;
        this.status = status;
        this.dateOfEntry = dateOfEntry;
        this.dateOfSale = dateOfSale;
        this.realEstateAgent = realEstateAgent;
    }




    // public Property(String type, String price, String surfaceOfProperty, String address) {
        //this.type = type;
       //this.price = price;
        //this.surfaceOfProperty = surfaceOfProperty;
        //this.address = address;
    //}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getSurfaceOfProperty() {
        return surfaceOfProperty;
    }

    public void setSurfaceOfProperty(int surfaceOfProperty) {
        this.surfaceOfProperty = surfaceOfProperty;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    //public String getPhoto() {
        //return photo;
    //}

    //public void setPhotos(String photo) {
        //this.photo = photo;
    //}


    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPointsOfInterest() {
        return pointsOfInterest;
    }

    public void setPointsOfInterest(String pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(Date dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public Date getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(Date dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public String getRealEstateAgent() {
        return realEstateAgent;
    }

    public void setRealEstateAgent(String realEstateAgent) {
        this.realEstateAgent = realEstateAgent;
    }
}
