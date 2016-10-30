package com.adnanali.foodish.Fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.adnanali.foodish.Activity.CheckOutActivity;
import com.adnanali.foodish.Adapter.CartRecAdapter;
import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Interface.CallBackInterface;
import com.adnanali.foodish.Interface.ConnectorInterface;
import com.adnanali.foodish.Model.Cart;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Utils.CommonHelper;

import java.util.List;

public class CartFragment extends Fragment implements CallBackInterface,View.OnClickListener{
    private TextView total;//, tvContinue;
    private View root;
    LinearLayout applyCouponCode;
    private RecyclerView rvCart;
    private List<BaseModel> productList;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cart, container, false);
        total = (TextView) root.findViewById(R.id.tvTotalPrice);

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabCheckOut);
        if (context instanceof ConnectorInterface) {
            ((ConnectorInterface) context).changeTitle(getResources().getString(R.string.cart));
        }
        rvCart = (RecyclerView) root.findViewById(R.id.rvCart);
        LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        rvCart.setLayoutManager(llm);

        if (productList != null) {
            showList();
        }else {
            productList = Cart.getProductsFromCart(context);
            if (productList != null) {
                showList();
            }
        }

        fab.setOnClickListener(this);

        onCallback();
        return root;
    }

    private void showList() {
        if (productList != null) {
            CartRecAdapter adapter = new CartRecAdapter(context, productList,false);
            adapter.setOnNotifyChange(this);
            adapter.notifyDataSetChanged();
            rvCart.setAdapter(adapter);
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onCallback() {
        String total = Cart.getTotalPrice(getContext(),true);
        this.total.setText(total);
    }

    @Override
    public void removeCart() {
       // getFragmentManager().popBackStackImmediate();
        if (context instanceof CheckOutActivity) {
            ((CheckOutActivity) context).finish();
        }

    }

    @Override
    public void removeItem(BaseModel model) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabCheckOut:
                if (CommonHelper.isRegistered(context)) {
                    if (context instanceof CheckOutActivity ) {
                        ((CheckOutActivity) context).changeFragment(true,new CheckoutFormSpecialFragment());
                    }
                }else{
                    LoginContainerFragment fragment = new LoginContainerFragment();
                    Bundle bundle = new Bundle();
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer,fragment)
                            .commit();
                }
                break;
        }
    }
}