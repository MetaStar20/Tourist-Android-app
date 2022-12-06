package com.tourism.attraction.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Attraction implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull

    private int id;

    @ColumnInfo(name = "name")
    private String  name;

    @ColumnInfo(name = "address")
    private String  address;

    @ColumnInfo(name = "photoName")
    private String  photoName;

    @ColumnInfo(name = "rating")
    private String  rating;

    @ColumnInfo(name = "number")
    private String  phoneNumber;

    @ColumnInfo(name = "note")
    private String  description;

    @ColumnInfo(name = "pricing")
    private String  pricing;

    @ColumnInfo(name = "favorite")
    private String favorite;

    public Attraction(int id, String name, String address, String phoneNumber, String description, String photoName, String rating,String pricing) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.photoName = photoName;
        this.rating = rating;
        this.pricing = pricing;
    }

    public Attraction(){
        name = address = phoneNumber = description = photoName = rating = pricing = "";
        favorite = "1";
    }

    public int getId() { return  this.id; }

    public void setId(int id) { this.id = id;  }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return this.address; }

    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return this.phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDescription() { return this.description; }

    public void setDescription(String description) { this.description = description; }

    public String getPhotoName() { return this.photoName; }

    public void setPhotoName(String photoName) { this.photoName = photoName; }

    public String getRating() { return this.rating; }

    public void setRating(String rating) { this.rating = rating; }

    public String getFavorite() { return this.favorite; }

    public void setFavorite(String favorite) { this.favorite = favorite; }

    public String getPricing() { return this.pricing; }

    public void setPricing(String pricing) { this.pricing = pricing; }

}
