package com.adnanali.foodish.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.adnanali.foodish.Activity.CheckOutActivity;
import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Interface.ConnectorInterface;
import com.adnanali.foodish.Model.Cart;
import com.adnanali.foodish.Model.CustomDictionary;
import com.adnanali.foodish.Model.ProductDetail;
import com.adnanali.foodish.Model.User;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Service.RestClient;
import com.adnanali.foodish.Utils.CommonHelper;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Adnan Ali on 10/17/2016.
 */

public class CheckoutFormSpecialFragment extends Fragment {
    View view;
    TextView tvBuyNow;
    TextView tvTotalPrice,tvSubTotal,tvDiscount;
    String productstring;
    CheckBox cbTermsAndCond;
    public static final String USER_TYPE= "USERtype";
    long price2;
    int loyalyPoints;
    EditText etFirstName, etLastName, etMobileBill, etAddressBill, etCityBill,etEmailBill;
    private Context context;
    List<BaseModel> productList;
    private int totalItemQuantity = 0;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_checkout_form_special, container, false);
        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        etMobileBill = (EditText) view.findViewById(R.id.etMobileBill);
        etAddressBill = (EditText) view.findViewById(R.id.etAddressBill);
        etCityBill = (EditText) view.findViewById(R.id.etCityBill);
        etEmailBill = (EditText) view.findViewById(R.id.etEmailBill);
        tvBuyNow = (TextView) view.findViewById(R.id.tvBuyNow);
        tvTotalPrice = (TextView) view.findViewById(R.id.tvTotalPrice);
        tvSubTotal = (TextView) view.findViewById(R.id.tvSubTotal);
        tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
        User user = CommonHelper.getUser(context);

        long price = Long.valueOf(Cart.getTotalPrice(context,false));
         price2 = (long) (price- (0.50 * user.getPoints()));
         loyalyPoints = (int) (price/10);



        tvTotalPrice.setText(price2 + "/-");
        tvSubTotal.setText(price+ "/-");
        tvDiscount.setText(user.getPoints()+"");

        cbTermsAndCond = (CheckBox) view.findViewById(R.id.cbTermsAndCond);


        cbTermsAndCond.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
//                    TermsAndConditionsDialog dialog = new TermsAndConditionsDialog(context);
//                    dialog.show();
                }else{

                }
            }
        });


        init();
        if (context instanceof ConnectorInterface) {
            ((ConnectorInterface) context).changeTitle("User Detail");
        }
//        Log.v("Total", Constants.total_price);

        if (productList == null) {
            productList = Cart.getProductsFromCart(context);
        }
        if (productList != null) {
            productstring = CommonHelper.getGson().toJson(productList) ;
            Log.v("Array", productstring);
        }

        for (int i = 0; i < productList.size(); i++) {
            totalItemQuantity += ((ProductDetail)productList.get(i)).getQuantity();
        }
        Log.v("total quantity",totalItemQuantity+"");



        tvBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                            if (productList != null && productList.size() > 0) { //&& addressList != null && addressString != null
                                if (cbTermsAndCond.isChecked()) {
                                    saveAddress();
                                    postCheckout();
                                }else{
                                    Snackbar.make(view,"Please Accept Terms and conditions", Snackbar.LENGTH_SHORT).show();
                                }

                            }else{
                                Toast.makeText(getContext(), "There is no items in cart.", Toast.LENGTH_SHORT).show();
                            }
                }
            }
        });


        return view;
    }

    private void postCheckout()

    {
        List<User> addressList = CommonHelper.getUsers(context);
        String productJson = CommonHelper.getGson().toJson(productList);
        String addressJson = CommonHelper.getUserInJson(context);
        User user =  CommonHelper.getUser(context);

        final ProgressDialog dialog = ProgressDialog.show(context,"Placing Order",CommonHelper.DIALOG_MESSAGE);
        RestClient.getApi().PostCheckout(productJson,
                user.getEmail(),
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) +"" ,
                price2+"",
                "Pending",
                loyalyPoints,

                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        dialog.dismiss();
                    //    Toast.makeText(context, "Your order id is "+jsonElement.toString(), Toast.LENGTH_SHORT).show();
                        if (jsonElement != null) {
                            try {
                                JSONObject obj = new JSONObject(jsonElement.toString());
                                if (obj != null && obj.getBoolean("status")) {
                                    if (context instanceof CheckOutActivity) {
                                        Toast.makeText(context, "Your order has been taken and order id is "+ obj.getString("data"), Toast.LENGTH_SHORT).show();
                                        if (context instanceof Activity) {
                                            ((CheckOutActivity) context).finish();
                                            Cart.clearCart(context);
                                        }
                                        //((CheckOutActivity) context).changeFragment(true,new ThankYouFragment(),new CustomDictionary(CommonHelper.ID,obj.getString("orderId")));
                                    }
                                }else{
                                    Toast.makeText(context, "Order is not taken", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Order is not taken", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(context, "Order is not taken", Toast.LENGTH_SHORT).show();
                        }
//
//                        try {
//
//
////                            Cart.clearCart(context);
////                            Intent intent = new Intent(context, MainActivity.class);
////                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                            startActivity(intent);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Toast.makeText(context, "Can not navigate to Home screen...", Toast.LENGTH_SHORT).show();
//                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        dialog.dismiss();
                        Toast.makeText(context, "Order is not taken \nPlease try again", Toast.LENGTH_SHORT).show();
                        Log.v("error",error.toString());

                    }
                }
        );
    }





    private boolean checkFields(){
        if (!(etFirstName.getText().toString().length() > 0)) {
            Toast.makeText(context, "Please enter first name", Toast.LENGTH_SHORT).show();
            return false;
        } if (!(etLastName.getText().toString().length() > 0)) {
            Toast.makeText(context, "Please enter last name", Toast.LENGTH_SHORT).show();
            return false;
        }  if (!(etAddressBill.getText().toString().length() > 0)) {
            Toast.makeText(context, "Please enter Address Line 1", Toast.LENGTH_SHORT).show();
            return false;
        }  if (!(etCityBill.getText().toString().length() > 0)) {
            Toast.makeText(context, "Please enter City", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(etEmailBill.getText().toString().length() > 6)) { // a@.com
            Toast.makeText(context, "Please enter Email name", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (!(etMobileBill.getText().toString().length() > 10)) {
//            Toast.makeText(context, "Please enter correct mobile number ", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        return true;
    }

    private void init() {

        if (CommonHelper.isRegistered(context)) {
            List<User> users = CommonHelper.getUsers(context);
            if (users != null && users.size() == 1) {
                etFirstName.setText(users.get(0).getFirst_name());
                etLastName.setText(users.get(0).getLast_name());
                etMobileBill.setText(users.get(0).getTelephone());
                etEmailBill.setText(users.get(0).getEmail());
                etAddressBill.setText(users.get(0).getStreet());
                etCityBill.setText(users.get(0).getCity());

            }
        }
    }

    private boolean saveAddress(){
        List<User>  users = new ArrayList<>();
        User user = new User();

        user.setFirst_name(etFirstName.getText().toString());
        user.setLast_name(etLastName.getText().toString());
        user.setStreet(etAddressBill.getText().toString());
        // user.setCountry_id(countrycode.get(spn_select_country.getSelectedItemPosition()));
        user.setCity(etCityBill.getText().toString());
        user.setTelephone(etMobileBill.getText().toString());
        user.setEmail(etEmailBill.getText().toString());

        users.add(0, user);

        if (CommonHelper.saveUsers(context,users)) {
            return true;
        }
        else{
            return false;
        }

    }

    private boolean setUsers() {

        if (CommonHelper.isRegistered(context)) {
            return true;
        }else{
            return saveAddress();

        }

    }





    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }


}

