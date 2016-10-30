package com.adnanali.foodish.Adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.adnanali.foodish.Activity.CheckOutActivity;
import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Interface.CallBackInterface;
import com.adnanali.foodish.Interface.CartListener;
import com.adnanali.foodish.Model.Cart;
import com.adnanali.foodish.Model.ProductDetail;
import com.adnanali.foodish.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adnan Ali on 8/29/2016.
 */
public class CartRecAdapter extends RecyclerView.Adapter<CartRecAdapter.ProductHolder> {

    private final String space = "   ";
    private CallBackInterface onNotifyChange;

    Context context;
    List<BaseModel> list;
    CartListener listener;
    boolean isDeleteEnable;
    private List<String> qtyList;
    DecimalFormat formatter;


    public CartRecAdapter(Context context, List<BaseModel> list, boolean isDeleteEnable) {
        this.context = context;
        this.list = list;
        this.isDeleteEnable = isDeleteEnable;
        if (context instanceof CartListener) {
            listener = (CartListener) context;
        }
        formatter = new DecimalFormat("#,###,###");
        qtyList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            qtyList.add(i + space);
        }

    }

    public void setOnNotifyChange(CallBackInterface onNotifyChange) {
        this.onNotifyChange = onNotifyChange;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        holder.tvProdName.setText(list.get(position).getName());

        try {
            holder.tvProdPrice.setText("Rs." + formatter.format(list.get(position).getPrice()));
            holder.tvProdTotal.setText("Rs." + formatter.format(list.get(position).getTotal()));
        } catch (Exception e) {
            e.printStackTrace();
            holder.tvProdPrice.setText("Rs." + list.get(position).getPrice());
            holder.tvProdTotal.setText("Rs." + list.get(position).getTotal());

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, qtyList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spProdQty.setAdapter(adapter);
        holder.spProdQty.setSelection(list.get(position).getQuantity() - 1);
        Picasso.with(context)
                .load(list.get(position).getImage())

                .error(R.drawable.logo).placeholder(R.drawable.logo)
                .into(holder.image);

    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ProductHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener, View.OnClickListener {
        ImageView image, ivDelete;//,ivArrowAdd,ivArrowLess;
        TextView tvProdName, tvProdPrice, tvProdTotal;
        Spinner spProdQty;

        public ProductHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ivProdImage);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            tvProdName = (TextView) itemView.findViewById(R.id.tvProdName);
            spProdQty = (Spinner) itemView.findViewById(R.id.spQty);
            tvProdPrice = (TextView) itemView.findViewById(R.id.tvProdPrice);
            tvProdTotal = (TextView) itemView.findViewById(R.id.tvProdTotal);
            spProdQty.setOnItemSelectedListener(this);
            ivDelete.setOnClickListener(this);



        }

        @Override
        public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
            updateQuantity(position + 1, getAdapterPosition());
        }

        @Override
        public void onNothingSelected (AdapterView < ? > parent){

        }

        @Override
        public void onClick (View v){
            deleteCurrentItem(getAdapterPosition());
        }

        private void deleteCurrentItem(int position) {
            if (context instanceof CartListener) {
                ((CartListener) context).onRemoveFromCart((ProductDetail) list.get(position));
            }
            list.remove(position);
            notifyItemRemoved(position);

            if (onNotifyChange != null) {
                onNotifyChange.onCallback();
            }


        }

        private void updateQuantity(int qty, int position) {
            ProductDetail detail = (ProductDetail) list.get(position);
            if (qty != detail.getQuantity()) {
                detail.setQuantity(qty);
                list.set(position, detail);
                notifyItemChanged(position);
                if (context instanceof CheckOutActivity) {
                    ((CheckOutActivity) context).onUpdateCart(detail);
                }
                if (onNotifyChange != null) {
                    onNotifyChange.onCallback();
                }
            }


        }


    }
}

