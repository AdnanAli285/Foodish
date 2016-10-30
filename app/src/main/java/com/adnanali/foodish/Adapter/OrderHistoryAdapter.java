package com.adnanali.foodish.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adnanali.foodish.Model.OrderHistory;
import com.adnanali.foodish.R;

import java.util.List;

/**
 * Created by Adnan Ali on 8/15/2016.
 */
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.Holder> {

    List<OrderHistory> orderHistories;
    Context context;

    public OrderHistoryAdapter(Context context, List<OrderHistory> categories) {
        this.orderHistories = categories;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.tvTotal.setText( "Rs." +orderHistories.get(position).getTotal());
        holder.tvDate.setText( orderHistories.get(position).getDate());
        holder.tvStatus.setText( orderHistories.get(position).getStatus());


    }



    @Override
    public int getItemCount() {
        return orderHistories != null ? orderHistories.size() : 0;
    }

     class Holder extends RecyclerView.ViewHolder{

         TextView tvTotal,tvStatus,tvDate;
         public Holder(View itemView) {
             super(itemView);
             tvTotal = (TextView) itemView.findViewById(R.id.tvTotal);
             tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
             tvDate = (TextView) itemView.findViewById(R.id.tvDate);


         }


     }
}
