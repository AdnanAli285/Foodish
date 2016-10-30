package com.adnanali.foodish.Model;

import com.adnanali.foodish.Interface.BaseModel;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Adnan Ali on 6/9/2016.
 */
public class ProductDetail extends BaseModel {
    public static final String CURRENCY_UNIT = "Rs.";
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    private String description;
    private String image;
    @SerializedName("qty")
    private int quantity;
    private long price;
    @SerializedName("total_price")
    private long total;

    public ProductDetail() {
    }

    public ProductDetail(String id, String name, String description, String image, int quantity, long price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public Long getPrice() {

        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public long getTotal() {
        return (price )*quantity;

    }

    public void setTotal(long total) {
        this.total = total;
    }
}