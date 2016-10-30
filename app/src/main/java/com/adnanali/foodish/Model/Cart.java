package com.adnanali.foodish.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Utils.CommonHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Adnan Ali on 4/26/2016.
 */
public class Cart {
    public static final String sharedData = "sharedData";

    public static <T extends BaseModel> boolean  addToCart(T product, Context context){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(sharedData, 0);
        SharedPreferences.Editor editor;
        editor = sp.edit();
        if (product instanceof ProductDetail) {
            ((ProductDetail) product).setDescription("");
        }
        try{
            editor.putString(product.getId(), CommonHelper.getGson().toJson(product));
            editor.commit();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static <T extends BaseModel> boolean removeFromCart(T product , Context context){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(sharedData, 0);
        sp.edit().remove(product.getId())
                .apply();
        if (sp.contains(product.getId())) {
            return false;
        }
        return true;
    }

    public static <T extends BaseModel> void updateCart(T product,Context context){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(sharedData, 0);
        SharedPreferences.Editor editor = sp.edit();
        if (sp.contains(product.getId())) {
            editor.putString( product.getId(),CommonHelper.getGson().toJson(product));
            editor.apply();
        }
        editor.commit();
    }

    public static String getProductsInJson(Context context){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(sharedData,0);
        Map<String,?> map = sp.getAll();
        for (Map.Entry<String,?> entry : map.entrySet()) {

        }
        return null;
    }

    public static Map<String,?> tempDelete(Context context){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(sharedData,0);
        Map<String,?> map = sp.getAll();
        SharedPreferences.Editor editor = sp.edit();
        for (Map.Entry<String,?> entry :map.entrySet()) {
            editor.remove(entry.getKey()).apply();
        }
        editor.commit();
        return map; // key is the productId and value is the productDetail
    }

    public static boolean insertDeletedProducts(Context context, Map<String,?> map){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(sharedData,0);
        SharedPreferences.Editor editor = sp.edit();
        for (Map.Entry<String,?> entry : map.entrySet()) {
            editor.putString(entry.getKey(),(String) entry.getValue());
            editor.apply();
        }
        editor.commit();

        return true;


    }

    public static <T extends BaseModel> List<T> getProductsFromCart(Context context){
       SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(sharedData,0);
        List<T> products = new ArrayList<>();
        Map<String,?> map = sp.getAll();
        for (Map.Entry<String,?> entry : map.entrySet()) {
            products.add((T) CommonHelper.getGson().fromJson((String)entry.getValue(),ProductDetail.class));
//            products.add((T) getCustomGson().fromJson((String)entry.getValue(),BaseModel.class));

//            products.add((T) (getBaseModel((String) entry.getValue())));
        }
        return products.size() > 0 ? products : null;

    }

    public static boolean clearCart(Context context){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(sharedData,0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        if (sp.getAll() == null || (sp.getAll() != null && sp.getAll().size() == 0) ) {
            return true;
        }return false;
    }

    public static int getProductCount(Context context){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(sharedData,0);
        Map<String,?> map = sp.getAll();
        return map!= null ? map.size() : 0;
    }

    public static String getTotalPrice(Context context,boolean isCurrencyAttach){
        SharedPreferences sp = context.getApplicationContext()
        .getSharedPreferences(sharedData, 0);
        long price = 0;
        for (Map.Entry<String,?> entry : sp.getAll().entrySet()) {
            ProductDetail detail = CommonHelper.getGson().fromJson((String)entry.getValue(),ProductDetail.class);
//            BaseModel detail = CommonHelper.getGson().fromJson((String)entry.getValue(),BaseModel.class);

//            BaseModel detail = (getBaseModel((String) entry.getValue()));

            price += detail.getTotal();

            Log.v("price",""+price);
         //   price +=  CommonHelper.getGson().fromJson((String)entry.getValue(),ProductDetail.class).getTotal();
        }
        Log.v("done","done");
//        DecimalFormat formatter = new DecimalFormat("#,###,###");
//        String formatedPrice = formatter.format(price);
        if (isCurrencyAttach) {
            return ProductDetail.CURRENCY_UNIT + price;
        }
        return price+"";

    }

//    private static BaseModel getBaseModel(String jsonString){
//        ProductDetail detail = new ProductDetail();
//        try {
//
//            JSONObject json = new JSONObject(jsonString);
//
//            if (json.has("id")) {
//                detail.setId(json.getString("id"));
//            } else if (json.has("product_id")) { // product detail
//                detail.setId(json.getString("product_id"));
//            }
//
//            if (json.has("images")) {
//                detail.setImage(json.getString("images"));
//            }else{
//                detail.setImage(json.getString("image"));
//            }
//
//
//
//            if (json.has("sku")) {
//                detail.setSku(json.getString("sku"));
//            }else{
//                detail.setSku("");
//            }
//
//            detail.setName(json.getString("name"));
//            detail.setQuantity(json.getInt("qty"));
//            detail.setPrice(json.getLong("price"));
//
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return detail;
//    }





}
