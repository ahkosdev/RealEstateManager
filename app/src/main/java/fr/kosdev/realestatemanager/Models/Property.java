package fr.kosdev.realestatemanager.Models;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Property implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String type;
    private String price;
    private String numberOfRooms;
    private String surfaceOfProperty;
    private String propertyDescription;
    private String photo;
    //private List<String> photos;
    private String address;
    private String pointsOfInterest;
    private String status;
    private String dateOfEntry;
    private long dateOfSale;
    private String realEstateAgent;

    public Property(long id, String photo, String type, String price, String numberOfRooms, String surfaceOfProperty, String propertyDescription, String address, String pointsOfInterest, String status, String dateOfEntry, String realEstateAgent) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.numberOfRooms = numberOfRooms;
        this.surfaceOfProperty = surfaceOfProperty;
        this.propertyDescription = propertyDescription;
        this.photo = photo;
        this.address = address;
        this.pointsOfInterest = pointsOfInterest;
        this.status = status;
        this.dateOfEntry = dateOfEntry;
       // this.dateOfSale = dateOfSale;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getSurfaceOfProperty() {
        return surfaceOfProperty;
    }

    public void setSurfaceOfProperty(String surfaceOfProperty) {
        this.surfaceOfProperty = surfaceOfProperty;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhotos(String photo) {
        this.photo = photo;
    }
    //public String getPhoto() {
        //return photo;
    //}

    //public void setPhoto(String photo) {
        //this.photo = photo;
    //}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(String dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public long getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(long dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public String getRealEstateAgent() {
        return realEstateAgent;
    }

    public void setRealEstateAgent(String realEstateAgent) {
        this.realEstateAgent = realEstateAgent;
    }
}
