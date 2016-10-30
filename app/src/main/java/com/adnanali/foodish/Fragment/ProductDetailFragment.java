package com.adnanali.foodish.Fragment;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adnanali.foodish.Activity.ProductDetailActivity;
import com.adnanali.foodish.Model.ProductDetail;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Utils.CommonHelper;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailFragment extends Fragment implements View.OnClickListener
        {


    DecimalFormat formatter;
    TextView tvProdName,tvQty,tvPrice;
    FloatingActionButton fabAddToCart;
    ImageView ivProduct,ivReload,ivQtyUp,ivQtyDown;
    ProductDetail productDetail;
    public static String PRODUCT_JSON = "productId";
    int quantity = 1;
    String id;
    ObjectAnimator reload;
   // public ProductDetailCallback detailCallback;
    private Context context;
    private NestedScrollView nestedScrollView;
    int height = 0;


    public ProductDetailFragment() {
        // Required empty public constructor
    }

    public static ProductDetailFragment getNewInstance( String productJson){
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_JSON,productJson);
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment



        View view =  inflater.inflate(R.layout.fragment_product_detail, container, false);
        tvProdName= (TextView) view.findViewById(R.id.tvProdName);
        tvQty = (TextView) view.findViewById(R.id.tvQty);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        fabAddToCart = (FloatingActionButton) view.findViewById(R.id.fabAddToCart);
        ivProduct = (ImageView) view.findViewById(R.id.ivProduct);
        ivReload = (ImageView) view.findViewById(R.id.ivReload);
        ivQtyDown = (ImageView) view.findViewById(R.id.ivQtyDown);
        ivQtyUp = (ImageView) view.findViewById(R.id.ivQtyUp);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        formatter = new DecimalFormat("#,###,###");
        fabAddToCart.setOnClickListener(this);
       // tvQty.setOnClickListener(this);
        ivReload.setOnClickListener(this);
        ivQtyDown.setOnClickListener(this);
        ivQtyUp.setOnClickListener(this);
        onQuantitySelect(quantity);
        height = context.getResources().getDisplayMetrics().heightPixels;



        try {
            if (reload == null) {
                reload = ObjectAnimator.ofFloat(ivReload,"rotation",0f,359f);
                reload.setDuration(3000);
                reload.setRepeatCount(ValueAnimator.INFINITE);
            }

            if (productDetail == null) {
                String json = getArguments().getString(PRODUCT_JSON);
                productDetail = CommonHelper.getGson().fromJson(json,ProductDetail.class);
            }
            setupViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public void onQuantitySelect(int qty){
        Log.v("qty",qty+"");
        tvQty.setText(qty+"");
       // productDetail.setSelectedQty(qty);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabAddToCart:
              //  Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                if (productDetail != null  ) {
                    productDetail.setQuantity(Integer.valueOf(tvQty.getText().toString()));
                    ((ProductDetailActivity)context).onAddToCart(productDetail);
                }else{
                    Toast.makeText(context, "No Detail found", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ivQtyDown:
                if (quantity > 1) {
                    quantity--;
                    onQuantitySelect(quantity);
                }
                break;

            case R.id.ivQtyUp:
                if (quantity < 20) {
                    quantity++;
                    onQuantitySelect(quantity);
                }
                break;
        }
    }

    private void setupViews() throws Exception{
        if (productDetail != null) {

            if (context instanceof ProductDetailActivity && productDetail != null) {

                ((ProductDetailActivity) context).changeTitle(productDetail.getName());
            }
            if (isAdded()) { // isAdded is fragment method to check if fragment is successfully added to activity
                tvProdName.setText(productDetail.getName());
                tvPrice.setText(ProductDetail.CURRENCY_UNIT+ formatter.format(productDetail.getPrice()) );

                Picasso.with(context)
                        .load(productDetail.getImage())
                        .error(R.drawable.logo)//.placeholder(R.drawable.ic_launcher)
                        .into(ivProduct);

            }
        }
    }

}
