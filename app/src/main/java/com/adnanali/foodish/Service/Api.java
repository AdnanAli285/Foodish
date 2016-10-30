package com.adnanali.foodish.Service;

import com.google.gson.JsonElement;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Adnan Ali on 4/20/2016.
 */
public interface Api {


    @GET("/search?type=hotels&fields=all")
    void GetRestaurants(Callback<JsonElement> callback);

    @GET("/search?type=orders&fields=all")
    void GetOrderHistory(@Query("email") String email , Callback<JsonElement> callback);





    @GET("/search?type=hotels&fields=dishes")
    void GetDishes(@Query("id") String restaurantId, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST("/rating")
    void RateRestaurant(@Field("id") String id,@Field("rate") int rating, Callback<JsonElement> callback);


    @FormUrlEncoded
    @POST("/signup")
    void RegisterUser(
            @Field("fname") String firstname,
            @Field("lname") String lastname,
            @Field("email") String email,
            @Field("password") String pass,
            @Field("address") String address,
            @Field("city") String city,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST("/login")
    void LoginUser(
            //@Query("type") String param1,
            @Field("email") String email,
            @Field("password") String pass,
            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST("/feedback.php")
    void SubmitFeedback(@Field("email") String email,
                        @Field("comment") String comment,
                        @Field("mobileVersion") String mobileVersion,
                        @Field("osVersion") String osVersion,
                        @Field("appVersion") String appVersion,
                        @Field("connectionMethod") String connectionMethod,
                        Callback<JsonElement> callback);




    @FormUrlEncoded
    @POST("/order")
    void PostCheckout(@Field("dishes") String dishes,
                      @Field("email") String email,
                      @Field("date") String date,
                      @Field("total") String total,
                      @Field("status") String status,
                      @Field("points") int loyaltyPoints,
                      Callback<JsonElement> callback
                      );

//    @FormUrlEncoded
//    @POST("/checkout.php")
//    void Post_Checkout
//            (@Field("product") String product,
//             @Field("address") String address,
//             @Field("ship_charges") String ship_charges,
//             @Field("payment_method") String payment_method,
//             @Field("ship_desc") String ship_desc,
//             @Field("customer_id") String customer_id,
//             @Field("customer_group_id") String customer_group_id,
//             @Field("shipping_method") String Shipping_method,
//             @Field("created_date") String created_date,
//             @Field("item_count") String item_count,
//             @Field("order_comment") String order_comment,
//             @Field("customer_email") String customer_email,
//             @Field("customer_firstname") String customer_firstname,
//             @Field("customer_lastname") String customer_lastname,
//             @Field("price") String price,
//
//             Callback<JsonElement> callback);

    void Search(String s, Callback<JsonElement> callback);

}
//    @Field("grand_total") String grandTotal,
//    @Field("device_id") String diviceId,