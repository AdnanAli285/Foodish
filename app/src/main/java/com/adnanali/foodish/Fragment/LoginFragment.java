package com.adnanali.foodish.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.adnanali.foodish.Activity.CheckOutActivity;
import com.adnanali.foodish.Interface.ConnectorInterface;
import com.adnanali.foodish.Model.User;
import com.adnanali.foodish.R;

import com.adnanali.foodish.Service.RestClient;
import com.adnanali.foodish.Utils.CommonHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Context context;
    private int GOOGLE_REQUEST_CODE = 1,FACEBOOK_REQUEST_CODE = 2;
    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        final EditText email = (EditText) view.findViewById(R.id.etEmail);
        final EditText password = (EditText) view.findViewById(R.id.etPassword);
      //  btnFb = (LoginButton) view.findViewById(R.id.btnFbSignIn);
        TextView tvLogin = (TextView) view.findViewById(R.id.tvLogin);
        TextView tvGuest = (TextView) view.findViewById(R.id.tvGuest);
        if (context instanceof ConnectorInterface) {
            ((ConnectorInterface) context).changeTitle(getResources().getString(R.string.login));
        }
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().length() > 0 && password.getText().length() > 0) {
                    loginUser(email.getText().toString(),password.getText().toString());
                }else{
                    Toast.makeText(getContext(),"Please fill all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof CheckOutActivity) {
                    ((CheckOutActivity) context).changeFragment(true,new CheckoutFormSpecialFragment());
                }
            }
        });








        return view;
    }


    private void onLoginSuccesful() {
        if (context instanceof CheckOutActivity && ((CheckOutActivity) context).isComingFromCart) {
            ((CheckOutActivity) context).changeFragment(true,new CheckoutFormSpecialFragment());
        }else{
            ((ConnectorInterface) context).removeFragment(LoginFragment.this);
        }
    }


    public void loginUser(String email, String password) {
        final ProgressDialog dialog = ProgressDialog.show(context,"Logging in", CommonHelper.DIALOG_MESSAGE);
        RestClient.getApi().LoginUser(email, password, new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                try {

                    dialog.dismiss();
                    Log.v("login",jsonElement.toString());

                    JSONObject jsonObject = new JSONObject(jsonElement.toString());
                    if (jsonObject.getBoolean("status")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        List<User> users = new ArrayList<User>();
                        User user = new User();
                        if (jsonArray != null && jsonArray.length() >0) {
                            Toast.makeText(getContext(), "Successfully Login", Toast.LENGTH_SHORT).show();
                            JSONObject object = jsonArray.getJSONObject(0);
                            user.setFirst_name(object.getString("fname"));
                            user.setLast_name(object.getString("lname"));
                            user.setEmail(object.getString("email"));
                            user.setStreet(object.getString("address"));
                            user.setCity(object.getString("city"));
                            user.setPoints(object.getInt("points"));

                            users.add(user);
                            CommonHelper.saveUsers(context,users);
//                            if (context instanceof ConnectorInterface) {
//                                ((ConnectorInterface) context).removeFragment(LoginFragment.this);
//                            }
                            onLoginSuccesful();
                        }
                        dialog.dismiss();

                        //dismiss();

                    } else {
                        Toast.makeText(getContext(), jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    //    CreateCart();
                  //  dismiss();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Un Successfully Login", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                  //  dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Log.v("login error",error.toString());
                dialog.dismiss();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
               // dismiss();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }


}
