package com.adnanali.foodish.Interface;

/**
 * Created by Adnan Ali on 8/16/2016.
 */
public abstract class BaseModel {
    public abstract String getId();
    public abstract String getName();
    public abstract  Object getPrice();
    public abstract String getImage();
    public abstract long getTotal();
    public abstract int getQuantity();
}
