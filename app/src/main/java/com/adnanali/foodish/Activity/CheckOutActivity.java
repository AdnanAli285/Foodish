package com.adnanali.foodish.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.adnanali.foodish.Enum.UserType;
import com.adnanali.foodish.Fragment.CartFragment;
import com.adnanali.foodish.Fragment.LoginContainerFragment;
import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Interface.CartListener;
import com.adnanali.foodish.Interface.CheckOutTypeListener;
import com.adnanali.foodish.Interface.ConnectorInterface;
import com.adnanali.foodish.Interface.MenuInteraction;
import com.adnanali.foodish.Model.Cart;
import com.adnanali.foodish.Model.CustomDictionary;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Utils.CommonHelper;


public class CheckOutActivity extends AppCompatActivity implements CartListener,
        ConnectorInterface,MenuInteraction {

    private TextView tvCartQuantity;
    Toolbar toolbar;
    public boolean isComingFromCart;
    @Override
    protected void onStart() {
        super.onStart();
        updateCartView();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        settingToolbar();



        if (getIntent().hasExtra(CommonHelper.USER_TYPE)) {
            UserType userType = (UserType) getIntent().getSerializableExtra(CommonHelper.USER_TYPE);
            switch (userType){
                case SignUp:
                    changeFragment(false,new LoginContainerFragment());
                    break;

                case Login:
                    changeFragment(false,new LoginContainerFragment());
                    break;

                case Cart:
                    changeFragment(false,new CartFragment());
                    isComingFromCart = true;
                    break;
            }



        }else{
            changeFragment(false,new CartFragment());
        }





    }
    private void settingToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Cart");

        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.pager_replacer));
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_cart).setVisible(false);
        menu.findItem(R.id.action_login).setVisible(false);
        menu.findItem(R.id.action_signup).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_clear_cart).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
               onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAddToCart(BaseModel detail) {

    }

    @Override
    public boolean onUpdateCart(BaseModel detail) {
        Cart.updateCart(detail,this);
        updateCartView();
        return true;
    }

    @Override
    public void onRemoveFromCart(BaseModel detail) {
            if (Cart.removeFromCart(detail,this)) {
               // Toast.makeText(this,detail.getName() + " removed from cart", Toast.LENGTH_SHORT).show();
                updateCartView();

            }
    }

    @Override
    public void updateCartView() {
        try {
            int count = Cart.getProductCount(this);
            if (count == 0) {
                tvCartQuantity.setVisibility(View.GONE);

            }
            else {
                tvCartQuantity.setVisibility(View.VISIBLE);
                tvCartQuantity.setText(count+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
           // Toast.makeText(this, "cart not updated ", Toast.LENGTH_SHORT).show();
        }
    }






    @Override
    public void changeFragment(boolean isAddToBackStack , Fragment fragment, CustomDictionary... dictionary) {

        Bundle bundle = null;
        if (dictionary != null && dictionary.length > 0) {
            bundle = new Bundle();
            for (CustomDictionary customDictionary : dictionary) {
                if (customDictionary.getValue() == null) {
                    bundle.putBoolean(customDictionary.getKey(),customDictionary.isBoolValue());
                }
                else{
                    bundle.putString(customDictionary.getKey(),customDictionary.getValue());
                }

            }
            fragment.setArguments(bundle);
        }

        FragmentTransaction fm = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,fragment,fragment.getClass().getSimpleName());
        if (isAddToBackStack) {
            fm.addToBackStack(fragment.getClass().getSimpleName());
        }
        fm.commit();
    }


    @Override
    public void removeFragment(Fragment fragment) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            onBackPressed();
        }else{
            getSupportFragmentManager().beginTransaction().remove(fragment);
        }


    }

    @Override
    public void changeTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void loginSuccess() {

    }
}
