package com.adnanali.foodish.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adnanali.foodish.R;
import com.adnanali.foodish.Utils.CommonHelper;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class StartUpActivity extends AppCompatActivity implements LocationListener{

    TextView tvLocation,tvDone;
    private LocationManager mLocationManager;
    ProgressDialog dialog;

    double latitude = 24.8738779;
    double longitude = 67.0798173;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvDone= (TextView) findViewById(R.id.tvDone);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartUpActivity.this,MainActivity.class));
                finish();
            }
        });

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requst();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    //dialog.dismiss();
                }
            }
        },3000);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        requst();
    }

    private void requst(){
        dialog = ProgressDialog.show(this,"Getting your Location","Please wait",false,true);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500,
                10, this);

        if (mLocationManager != null) {

            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                tvLocation.setText(getAddress(location.getLatitude(),location.getLongitude()));
                CommonHelper.setLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
            }else{
                tvLocation.setText(getAddress(latitude,longitude));
                CommonHelper.setLatLng(new LatLng(latitude,longitude));
                Toast.makeText(this, "Couldn't get your location", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
            //updateGPSCoordinates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        dialog.dismiss();
        tvLocation.setText(getAddress(location.getLatitude(),location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        dialog.dismiss();
    }

    @Override
    public void onProviderEnabled(String s) {
        dialog.dismiss();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Gps is disabled Please turn it on", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            return add +" " +obj.getSubAdminArea();
//
//            GUIStatics.currentAddress = obj.getSubAdminArea() + ","
//                    + obj.getAdminArea();
//            GUIStatics.latitude = obj.getLatitude();
//            GUIStatics.longitude = obj.getLongitude();
//            GUIStatics.currentCity= obj.getSubAdminArea();
//            GUIStatics.currentState= obj.getAdminArea();
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();
//
//            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, "Could not get location", Toast.LENGTH_SHORT).show();
            return "Try Again";
        }
    }

}
