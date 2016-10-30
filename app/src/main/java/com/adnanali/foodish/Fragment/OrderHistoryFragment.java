package com.adnanali.foodish.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adnanali.foodish.Adapter.OrderHistoryAdapter;
import com.adnanali.foodish.Model.OrderHistory;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Service.RestClient;
import com.adnanali.foodish.Utils.CommonHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistoryFragment extends Fragment {


    private RecyclerView rvOrder;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order_history, container, false);

        rvOrder = (RecyclerView) view.findViewById(R.id.rvOrder);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rvOrder.setLayoutManager(llm);
        getOrderHistory();




        return view;
    }

    private void getOrderHistory(){
        final ProgressDialog dialog = ProgressDialog.show(getActivity(),"Getting History",CommonHelper.DIALOG_MESSAGE);
        RestClient.getApi().GetOrderHistory(CommonHelper.getUser(getActivity()).getEmail(), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                dialog.dismiss();
                try {
                    List<OrderHistory> histories = CommonHelper.getGson().fromJson(jsonElement.toString(),new TypeToken<List<OrderHistory>>(){}.getType());
                    if (histories != null) {
                        OrderHistoryAdapter adapter = new OrderHistoryAdapter(getActivity(),histories);
                        adapter.notifyDataSetChanged();
                        rvOrder.setAdapter(adapter);
                    }else{
                        Toast.makeText(getActivity(), "Not available", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Not available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
