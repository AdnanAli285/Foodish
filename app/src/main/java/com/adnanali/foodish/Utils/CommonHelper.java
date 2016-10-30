package com.adnanali.foodish.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adnan Ali on 8/16/2016.
 */
public class CommonHelper {

    public static final String TYPE= "type";
    public static final String USER_TYPE= "USER_type";
    public static final String TITLE = "TITLE";
    public static final String DIALOG_MESSAGE = "Please wait...";
    public static final String ID = "id";
    public static final String CAT = "CAT";
    public static  final String  isFirst = "lsfkdjaslfj";



    public static final String Currency = "Rs.";
    public static final String USER_KEY = "ADDRSS.key";
    public static final String USER = "ADDRSS.";
    public static boolean isTimerOn = false;

    public static boolean isBuild = false;




    public static final String CASH= "cashondelivery";
    public static final String BANK= "Bank Transfer";
    private static String paymentMethod = CASH;

    private static LatLng latLng;

    public static LatLng getLatLng() {
        return latLng;
    }

    public static BaseModel[] restaurants;

    public static BaseModel[] getRestaurants() {
        return restaurants;
    }

    public static void setRestaurants(BaseModel[] restaurants) {
        CommonHelper.restaurants = restaurants;
    }

    public static void setLatLng(LatLng latLng) {
        CommonHelper.latLng = latLng;
    }

    private static List<BaseModel> recentList = new ArrayList<>();



    public static void setPaymentMethod(String method){
        paymentMethod = method;
    }
    public static String getPaymentMethod(){

        return paymentMethod;
    }


    private static Gson gson;
    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }


    public static void addToRecent(BaseModel baseModel){
        if (!recentList.contains(baseModel)) {
            recentList.add(baseModel);
        }
    }
    public static List<BaseModel> getRecentList(){
        return recentList;
    }

    public static boolean saveUsers(Context context, List<User> addresses){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(USER, 0);
        SharedPreferences.Editor editor;
        editor = sp.edit();
        try{
            editor.putString(USER_KEY, CommonHelper.getGson().toJson(addresses));
            editor.commit();
            return true;
        }catch (Exception e){
            return false;
        }
    }



    public static String getUserInJson(Context context){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(USER, 0);
        try{
            if (sp.contains(USER_KEY)) {
                String json =sp.getString(USER_KEY,null);
                return json;
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }

    public static List<User> getUsers(Context context){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(USER, 0);
        try{
            if (sp.contains(USER_KEY)) {
                String json =sp.getString(USER_KEY,null);
                if (json != null) {
                    List<User> addresses = CommonHelper.getGson().fromJson(json,new TypeToken<List<User>>(){}.getType()) ;
                    return addresses;
                }else{
                    return null;
                }
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }
    public static User getUser(Context context){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(USER, 0);
        try{
            if (sp.contains(USER_KEY)) {
                String json =sp.getString(USER_KEY,null);
                if (json != null) {
                    List<User> addresses = CommonHelper.getGson().fromJson(json,new TypeToken<List<User>>(){}.getType()) ;
                    return (addresses != null && addresses.size() > 0) ? addresses.get(0) : null;
                }else{
                    return null;
                }
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }

    public static void removeUser(Context context){
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(USER,0);
        if (sp.contains(USER_KEY)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(USER_KEY);
            editor.apply();
            editor.commit();
        }
    }

    public static boolean isRegistered(Context context){
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(USER, 0);
        try{
            if (sp.contains(USER_KEY)) {
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }

    }
}
