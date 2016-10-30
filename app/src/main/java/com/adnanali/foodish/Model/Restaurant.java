package com.adnanali.foodish.Model;

import android.location.Location;

import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Utils.CommonHelper;
import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

/**
 * Created by AdnanAli on 10/29/2016.
 */

public class Restaurant extends BaseModel{

    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private String town;
    private String image;
    @SerializedName("description")
    private String desc;
    private int rating;
    private double distance;

    private Review reviews;


    @SerializedName("categories")
    private Category[] categories;


    public Review getReviews() {
        return reviews;
    }

    public void setReviews(Review reviews) {
        this.reviews = reviews;
    }

    public Category[] getCategories() {
        return categories;
    }

    public void setCategories(Category[] categories) {
        this.categories = categories;
    }

    public Restaurant(String id, String name, String desc, String image, String town, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.town = town;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getDistance( double lat2,double long2) {
        if (CommonHelper.getLatLng() != null) {
            float[] result = new float[1];
            Location.distanceBetween(CommonHelper.getLatLng().latitude,CommonHelper.getLatLng().longitude,lat2,long2,result);
            return roundTwoDecimals(result[0]);
        }else return 0;

    }

    public double getDistance() {
        if (CommonHelper.getLatLng() != null) {
            float[] result = new float[1];
            Location.distanceBetween(CommonHelper.getLatLng().latitude,CommonHelper.getLatLng().longitude,latitude,longitude,result);
            return roundTwoDecimals(result[0]);
        }else return 0;

    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object getPrice() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getImage() {
        return image;
    }

    @Override
    public long getTotal() {
        return 0;
    }

    @Override
    public int getQuantity() {
        return 0;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
