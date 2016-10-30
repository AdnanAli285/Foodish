package com.adnanali.foodish.Fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adnanali.foodish.BuildConfig;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Service.RestClient;
import com.adnanali.foodish.Utils.CommonHelper;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {


    public FeedbackFragment() {
        // Required empty public constructor
    }

    EditText etEmail,etFeedback;

    CheckBox chbModelNo,chbOsVersion,chbAppVersion,chbConnMethod;
    TextView tvModelNo,tvOsVersion,tvAppVersion,tvConnMethod,tvSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        chbModelNo = (CheckBox) view.findViewById(R.id.chbModelNo);
        chbOsVersion = (CheckBox) view.findViewById(R.id.chbOsVersion);
        chbAppVersion = (CheckBox) view.findViewById(R.id.chbAppVersion);
        chbConnMethod = (CheckBox) view.findViewById(R.id.chbConnMethod);

        tvModelNo = (TextView) view.findViewById(R.id.tvModelNo);
        tvOsVersion = (TextView) view.findViewById(R.id.tvOsVersion);
        tvAppVersion = (TextView) view.findViewById(R.id.tvAppVersion);
        tvConnMethod = (TextView) view.findViewById(R.id.tvConnMethod);


        tvSend = (TextView) view.findViewById(R.id.tvSend);

        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etFeedback = (EditText) view.findViewById(R.id.etFeedback);

        if (CommonHelper.isRegistered(getActivity())) {
            etEmail.setText(CommonHelper.getUser(getActivity()).getEmail());
        }

        tvModelNo.setText(Build.MODEL);
        tvOsVersion.setText(Build.VERSION.RELEASE);
        tvAppVersion.setText(BuildConfig.VERSION_CODE+"");
        tvConnMethod.setText(getConnectionMethod());

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    submitFeedback(etEmail.getText().toString(),etFeedback.getText().toString(),tvConnMethod.getText().toString());
                }
            }
        });


        return view;
    }

    private String getConnectionMethod() {
        ConnectivityManager connectivitymanager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = connectivitymanager.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (netInfo.isConnected())
                    return "WiFi";

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (netInfo.isConnected())
                    return "Mobile Data";

        }
        return "None";
    }

    private boolean checkFields() {
        if (!(etEmail.getText().toString().length() > 2)) {
            Toast.makeText(getActivity(), "Please provide correct email address", Toast.LENGTH_SHORT).show();
            return false;
        }if (etFeedback.getText().toString().length() < 10) {
            Toast.makeText(getActivity(), "Please provide feedback with at least 10 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void submitFeedback(String email,String feedback,String connMethod){
        RestClient.getApi().SubmitFeedback(email,
                feedback,
                chbModelNo.isChecked() ? Build.MODEL : "",// mobilre Version
                chbOsVersion .isChecked() ? Build.VERSION.RELEASE : "", // osversion
                chbAppVersion.isChecked() ? BuildConfig.VERSION_CODE+"" : "", // app version
                chbConnMethod.isChecked() ? connMethod : "", // network connection method
                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonElement.toString());
                            if (jsonObject.getBoolean("status")) {
                                Toast.makeText(getActivity(), "Your Feedback has been submitted", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Your Feedback is not submitted", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Your Feedback is not submitted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), "Your Feedback is not submitted", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
