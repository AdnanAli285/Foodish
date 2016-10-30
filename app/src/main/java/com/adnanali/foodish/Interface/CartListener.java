package com.adnanali.foodish.Interface;

/**
 * Created by Adnan Ali on 4/21/2016.
 */
public interface CartListener {
    void onAddToCart(BaseModel detail);
    boolean onUpdateCart(BaseModel detail);
    void onRemoveFromCart(BaseModel detail);
    void updateCartView();

}
