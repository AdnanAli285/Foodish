package com.adnanali.foodish.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.adnanali.foodish.Enum.DrawerType;
import com.adnanali.foodish.Enum.Type;
import com.adnanali.foodish.Enum.UserType;
import com.adnanali.foodish.Fragment.HomeFragment;
import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Interface.CartListener;
import com.adnanali.foodish.Interface.ConnectorInterface;
import com.adnanali.foodish.Interface.MenuInteraction;
import com.adnanali.foodish.Model.Cart;
import com.adnanali.foodish.Model.CustomDictionary;
import com.adnanali.foodish.Model.User;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Utils.CommonHelper;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements ConnectorInterface,CartListener,MenuInteraction {
    private Menu menu;
    private Toolbar toolbar;
    private TextView tvCartQuantity;
    private CoordinatorLayout rootLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView tvUser,tvUserEmail,tvSignIn,tvFirstLetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        settingToolbar();
        settingNavigationView();
      //  checkDuplicate();

//        changeFragment(false,new MainFragment(),
//                new CustomDictionary(CommonHelper.isFirst, true));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new HomeFragment()).commit();

    }




    private void openDrawerActivity(DrawerType type){
        Intent intentSearch = new Intent(MainActivity.this,DrawerActivity.class);
        intentSearch.putExtra(CommonHelper.TYPE, type);
        startActivity(intentSearch);
    }

    private void settingNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navView);
        View view = navigationView.getHeaderView(0);
        if (view != null) {
            tvUser = (TextView) view.findViewById(R.id.tvUser);
            tvUserEmail = (TextView) view.findViewById(R.id.tvUserEmail);
            tvSignIn = (TextView) view.findViewById(R.id.tvSignIn);
            tvFirstLetter = (TextView) view.findViewById(R.id.tvFirstLetter);
            RelativeLayout llUser = (RelativeLayout) view.findViewById(R.id.llUser);
            llUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(MainActivity.this,CheckOutActivity.class);
                    intent1.putExtra(CommonHelper.USER_TYPE, UserType.SignUp);
                    startActivity(intent1);
                }
            });
            setUserName();
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                closeDrawer();
                item.setChecked(true);
                String id = null;
                String cat = null;
                switch (item.getItemId()){
                    case R.id.action_home:
                        try {
                            getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.order_history:

                        if (CommonHelper.isRegistered(MainActivity.this)) {
                            openDrawerActivity(DrawerType.ORDER_HISTORY);
                        }else{
                            Toast.makeText(MainActivity.this, "No history available", Toast.LENGTH_SHORT).show();
                        }
                        break;


                    case R.id.search:
                        openDrawerActivity(DrawerType.SEARCH);
                        break;

//                    case R.id.myAccount:
//                        openDrawerActivity(DrawerType.ACCOUNT);
//                        break;

                    case R.id.about_us:
                        openDrawerActivity(DrawerType.ABOUT_US);
                        break;

                    case R.id.feedback:

                        openDrawerActivity(DrawerType.FEEDBACK);
                        break;

                    case R.id.sign_out:
                        CommonHelper.removeUser(MainActivity.this);
                        if (menu != null) {
                            menu.findItem(R.id.action_login).setVisible(true);
                            menu.findItem(R.id.action_signup).setVisible(true);
                            menu.findItem(R.id.action_logout).setVisible(false);
                            onLogOut();
                            setUserName();
                        }
                        break;

                }

                return true;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        updateCartView();
        loginSuccess();
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeDrawer();
    }

    private void settingToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Foodish");
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void closeDrawer(){
        drawerLayout.closeDrawer(GravityCompat.START);
//        checkDuplicate();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        this.menu = menu;
        menu.findItem(R.id.action_logout).setVisible(false);

        MenuItem item = menu.findItem(R.id.action_cart);

        RelativeLayout layout = (RelativeLayout) item.getActionView();
        tvCartQuantity = (TextView) layout.findViewById(R.id.tvCartQuantity);
        updateCartView();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cart.getProductCount(MainActivity.this) == 0) {
                    //Toast.makeText(MainActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                    Snackbar.make(rootLayout,"Cart is empty", Snackbar.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity.this,CheckOutActivity.class);
                    intent.putExtra(CommonHelper.USER_TYPE, UserType.Cart);
                    startActivity(intent);
                }
            }
        });
        setupUser();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.action_login:
                Intent intent = new Intent(this,CheckOutActivity.class);
                intent.putExtra(CommonHelper.USER_TYPE, UserType.Login);
                startActivity(intent);
                break;

            case R.id.action_signup:
                Intent intent1 = new Intent(this,CheckOutActivity.class);
                intent1.putExtra(CommonHelper.USER_TYPE, UserType.SignUp);
                startActivity(intent1);
                break;

            case R.id.action_clear_cart:
                Snackbar snackbar = Snackbar.make(rootLayout,"All items have been removed from cart", Snackbar.LENGTH_LONG);
                final Map<String,?> map = Cart.tempDelete(MainActivity.this);
                updateCartView();
                if (map != null && map.size() > 0) {
                    snackbar.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Cart.insertDeletedProducts(MainActivity.this,map)) {
                                Snackbar.make(rootLayout,"Undone Successfully", Snackbar.LENGTH_SHORT).show();
                                updateCartView();
                            }else{
                                Snackbar.make(rootLayout,"Can not Undo", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                    snackbar.show();
                }else{
                    Snackbar.make(rootLayout,"Cart is empty", Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.action_logout:
                CommonHelper.removeUser(this);
                if (menu != null) {
                    menu.findItem(R.id.action_login).setVisible(true);
                    menu.findItem(R.id.action_signup).setVisible(true);
                    menu.findItem(R.id.action_logout).setVisible(false);
                    onLogOut();
                    setUserName();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void onLogOut() {
//        Cart.clearCart(this);
//        updateCartView();
        Snackbar snackbar = Snackbar.make(rootLayout,"You have been logged out", Snackbar.LENGTH_LONG);
        snackbar.setAction("Login", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoginDialog loginDialog = new LoginDialog(MainActivity.this);
//                loginDialog.show();
            }
        });
        snackbar.show();
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
            fm.addToBackStack(null);
        }
        fm.commit();
    }


    @Override
    public void removeFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void changeTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onAddToCart(BaseModel detail) {
        if (Cart.addToCart(detail, this)) {
            Snackbar.make(rootLayout,detail.getName()+" Added to Cart", Snackbar.LENGTH_LONG).show();
//            if (vibrator != null) {
//                vibrator.vibrate(100);
//            }
//
//            if (cartAnimator != null) {
//                cartAnimator.start();
//            }
            //  Toast.makeText(this,detail.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            updateCartView();

        }
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
                if(tvCartQuantity!= null ) tvCartQuantity.setVisibility(View.GONE);
            }
            else {
                if (tvCartQuantity != null) {
                    tvCartQuantity.setVisibility(View.VISIBLE);
                    tvCartQuantity.setText(count+"");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            //Toast.makeText(this, "cart not updated ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loginSuccess() {
       setupUser();
    }

    private void setupUser(){
        if (menu != null) {  // when it is called when activity is started first time menu will be null
            if (CommonHelper.isRegistered(this)) {
                menu.findItem(R.id.action_login).setVisible(false);
                menu.findItem(R.id.action_signup).setVisible(false);
                menu.findItem(R.id.action_logout).setVisible(true);
            }else{
                menu.findItem(R.id.action_login).setVisible(true);
                menu.findItem(R.id.action_signup).setVisible(true);
                menu.findItem(R.id.action_logout).setVisible(false);
            }

        }
        setUserName();
    }

    private void setUserName(){
        if (tvUser != null && tvUserEmail != null) {
            if (CommonHelper.isRegistered(this)) {
                User user = CommonHelper.getUser(this);
                String name = toPascalCase( (user.getFirst_name() != null ? user.getFirst_name() : "" )+ " " + (user.getLast_name() != null ? user.getLast_name() : ""));
                tvUser.setText(name);
                tvUser.setVisibility(View.VISIBLE);
                tvUserEmail.setVisibility(View.VISIBLE);
                tvSignIn.setVisibility(View.GONE);
                tvUserEmail.setText(user.getEmail());
                if (name != null && name.length() > 0) {
                    tvFirstLetter.setText(name.substring(0,1));
                }

            }else{
                //tvUser.setText(getResources().getString(R.string.user));
                tvUserEmail.setVisibility(View.GONE);
                tvUser.setVisibility(View.GONE);
                tvFirstLetter.setText("i");
                tvSignIn.setVisibility(View.VISIBLE);
            }
        }
    }

    private String toPascalCase(String text){
        boolean nextPascal = true;
        StringBuilder builder = new StringBuilder();

        if (text != null && text.length() > 0) {
            char[] array = text.toCharArray();
            for (char c  : array) {
                if (Character.isSpaceChar(c)) {
                    nextPascal = true;
                }
                else if (nextPascal) {
                    c = Character.toTitleCase(c);
                    nextPascal = false;
                }
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
