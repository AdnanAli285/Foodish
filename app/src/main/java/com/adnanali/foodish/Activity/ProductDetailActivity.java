package com.adnanali.foodish.Activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adnanali.foodish.Adapter.DetailPagerAdapter;
import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Interface.CartListener;
import com.adnanali.foodish.Interface.ProductDetailCallback;
import com.adnanali.foodish.Model.Cart;
import com.adnanali.foodish.R;


public class ProductDetailActivity extends AppCompatActivity implements ProductDetailCallback,CartListener {

    ViewPager detailPager;
    public static final String PRODUCT_JSON = "productIds";
    public static final String CURRENT_PRODUCT_INDEX = "productiNDEX";
    private int selectedProductIndex = 0;
    private TextView tvCartQuantity;
    private RelativeLayout rootLayout;
    Toolbar toolbar;
    Vibrator vibrator;

    ObjectAnimator cartAnimator;

    @Override
    protected void onStart() {
        super.onStart();
        updateCartView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        detailPager = (ViewPager) findViewById(R.id.vpProdDetail);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        DetailPagerAdapter adapter = new DetailPagerAdapter(getSupportFragmentManager(), getIntent().getStringArrayListExtra(PRODUCT_JSON));
//        DetailPagerAdapter adapter = new DetailPagerAdapter(getSupportFragmentManager(), getIntent().getStringExtra(PRODUCT_JSON));
            selectedProductIndex = getIntent().getExtras().getInt(CURRENT_PRODUCT_INDEX,0);
            detailPager.setAdapter(adapter);
            detailPager.setCurrentItem(selectedProductIndex);


        if (vibrator == null) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }

        toolbar.inflateMenu(R.menu.main_menu);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.pager_replacer));
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_login).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_signup).setVisible(false);
        menu.findItem(R.id.action_clear_cart).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(true);


        MenuItem item = menu.findItem(R.id.action_cart);
        RelativeLayout layout = (RelativeLayout) item.getActionView();
        tvCartQuantity = (TextView) layout.findViewById(R.id.tvCartQuantity);

        PropertyValuesHolder holder = PropertyValuesHolder.ofFloat("scaleX",1.8f);
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("scaleY",1.8f);

        cartAnimator = ObjectAnimator.ofPropertyValuesHolder(tvCartQuantity,holder,holder1);
        cartAnimator.setDuration(1000);
        cartAnimator.setRepeatCount(1);
        cartAnimator.setRepeatMode(ValueAnimator.REVERSE);
        cartAnimator.setInterpolator(new BounceInterpolator());
        cartAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

               // cartAnimator.reverse();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open Check out activity
                if (Cart.getProductCount(ProductDetailActivity.this) == 0) {
                    Snackbar.make(rootLayout,"Cart is empty", Snackbar.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(ProductDetailActivity.this, CheckOutActivity.class));
                }
            }
        });
        updateCartView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.action_share:

                String shareBody = "Product link will be here";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Foodish");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddToCart(BaseModel detail) {
        if (Cart.addToCart(detail, this)) {
           Snackbar.make(rootLayout,detail.getName()+" Added to Cart", Snackbar.LENGTH_LONG).show();
            if (vibrator != null) {
                vibrator.vibrate(100);
            }

            if (cartAnimator != null) {
                cartAnimator.start();
            }
          //  Toast.makeText(this,detail.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            updateCartView();

        }
    }



    @Override
    public boolean onUpdateCart(BaseModel detail) {
        return false;
    }

    @Override
    public void onRemoveFromCart(BaseModel detail) {

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
            //Toast.makeText(this, "cart not updated ", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onChangeProduct(String price) {
        Toast.makeText(this,price, Toast.LENGTH_SHORT).show();
    }
    public void changeTitle(String title){
        if (toolbar != null) {
            toolbar.setTitle(title);
        }

    }
}
