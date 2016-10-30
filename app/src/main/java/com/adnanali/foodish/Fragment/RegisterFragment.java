package com.adnanali.foodish.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.adnanali.foodish.Model.User;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Service.RestClient;
import com.adnanali.foodish.Utils.CommonHelper;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    public RegisterFragment() {
        // Required empty public constructor
    }
    EditText etFirstName, etLastName,etEmailBill,etPassword,etAddress,etCity;
    TextView tvCreateAccount;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);


        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        etEmailBill = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etCity = (EditText) view.findViewById(R.id.etCity);

        tvCreateAccount = (TextView) view.findViewById(R.id.tvCreateAccount);


        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    registerUser(etFirstName.getText().toString(),
                            etLastName.getText().toString(),
                            etEmailBill.getText().toString(),
                            etPassword.getText().toString(),
                            etAddress.getText().toString(),
                            etCity.getText().toString()

                            );
                }
            }
        });
        return view;
    }

    private boolean checkFields(){
        if (!(etFirstName.getText().toString().length() > 0)) {
            Toast.makeText(getContext(), "Please enter first name", Toast.LENGTH_SHORT).show();
            return false;
        } if (!(etLastName.getText().toString().length() > 0)) {
            Toast.makeText(getContext(), "Please enter last name", Toast.LENGTH_SHORT).show();
            return false;
        }if (!(etAddress.getText().toString().length() > 0)) {
            Toast.makeText(getContext(), "Please enter Address", Toast.LENGTH_SHORT).show();
            return false;
        }if (!(etCity.getText().toString().length() > 0)) {
            Toast.makeText(getContext(), "Please enter City", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(etEmailBill.getText().toString().length() > 6)) { // a@.com
            Toast.makeText(getContext(), "Please enter Email name", Toast.LENGTH_SHORT).show();
            return false;
        } if (!(etPassword.getText().toString().length() > 3)) {
            Toast.makeText(getContext(), "Password should be atleast 4 character", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean saveUsers(){
        List<User> users = new ArrayList<>();
        User user = new User();

        user.setFirst_name(etFirstName.getText().toString());
        user.setLast_name(etLastName.getText().toString());
        user.setEmail(etEmailBill.getText().toString());
        user.setStreet(etAddress.getText().toString());
        user.setCity(etCity.getText().toString());
        users.add(0, user);

        if (CommonHelper.saveUsers(getContext(),users)) {
            return true;
        }
        else{
            return false;
        }
    }

    private void registerUser(String first_name, String last_name, String email, String password,String address,String city) {
        final ProgressDialog dialog = ProgressDialog.show(getContext(),"Creating Account", CommonHelper.DIALOG_MESSAGE);
        RestClient.getApi().RegisterUser(first_name, last_name, email, password,address,city, new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                try {
                    dialog.dismiss();
                    Log.v("register",jsonElement.toString());
                    JSONObject jsonObject = new JSONObject(jsonElement.toString());
                    if (jsonObject.getBoolean("status")) {
                        if (saveUsers()) {
                            getActivity().finish();
                            Toast.makeText(getContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Un Successfully Registered", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    //  dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                dialog.dismiss();
                Log.v("register error",error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                //  dismiss();
            }
        });

    }

}
