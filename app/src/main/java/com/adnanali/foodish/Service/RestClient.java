package com.adnanali.foodish.Service;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Adnan Ali on 4/20/2016.
 */
public class RestClient {
//    public static final String BASE_URL = "http://www.zubaidashomestore.com";
    public static final String BASE_URL = "http://scayle.herokuapp.com/api";

    private static Api api;
    static {
        setup();
    }
    static void setup(){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient(okHttpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL);
        RestAdapter adapter  = builder.build();
        api = adapter.create(Api.class);

    }


    public static Api getApi() {
        return api;
    }

}
