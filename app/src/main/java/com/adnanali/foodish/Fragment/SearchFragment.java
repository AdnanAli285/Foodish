package com.adnanali.foodish.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adnanali.foodish.Adapter.GenericRecViewAdapter;
import com.adnanali.foodish.Enum.Type;
import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Model.Restaurant;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Service.RestClient;
import com.adnanali.foodish.Utils.CommonHelper;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    private Context context;

    public SearchFragment() {
        // Required empty public constructor
    }

    EditText etSearch;
    RecyclerView rvProducts;
    Spinner spinner;
    String distance = "Distance";
    String category = "Category";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        rvProducts = (RecyclerView) view.findViewById(R.id.rvProducts);
        spinner = (Spinner) view.findViewById(R.id.spinner);

        init();
        String[] spList = {distance,category};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,spList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rvProducts.setLayoutManager(llm);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    private void showList(BaseModel[] list){

        if (list != null && list.length > 0) {
            GenericRecViewAdapter adapter = new GenericRecViewAdapter(context,R.layout.restaurant_item,list, Type.Restaurants);
            adapter.notifyDataSetChanged();
            rvProducts.setAdapter(adapter);
        }else{
            Toast.makeText(context, "No Restaurant found", Toast.LENGTH_SHORT).show();
        }
    }

    private void search() {
        if (spinner.getSelectedItem().equals(distance)) {
            try {
                double distance =Long.valueOf(etSearch.getText().toString());
                if (distance > 0) {
                    searchByDistance(distance * 1000);
                }else{
                    Toast.makeText(context, "Distance should be greater than 0", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(context, "Please provide correct distance or select Category", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            searchByCategory(etSearch.getText().toString());
        }

    }

    private void searchByCategory(String category) {
        BaseModel[] list = CommonHelper.getRestaurants();
        List<BaseModel> currList = new ArrayList<>();
        if (list != null && list.length > 0) {
            for (BaseModel model : list) {
                Restaurant restaurant = (Restaurant) model;
                if (restaurant.getCategories() != null && restaurant.getCategories().length > 0) {
                    for (int i = 0 ; i < restaurant.getCategories().length; i++) {
                        if (restaurant.getCategories()[i].getName().contains(category)) {
                            currList.add(model);
                        }
                    }
                }
            }
        }
        showList(currList.toArray(new Restaurant[currList.size()]));
    }

    private void searchByDistance(double distance) {
        BaseModel[] list = CommonHelper.getRestaurants();
        List<BaseModel> currList = new ArrayList<>();
        if (list != null && list.length > 0) {
            for (BaseModel model : list) {
                Restaurant restaurant = (Restaurant) model;
                if (restaurant.getDistance() < distance) {
                    currList.add(model);
                }
            }
        }
        showList(currList.toArray(new Restaurant[currList.size()]));
    }

    private void init(){
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (etSearch.getText() != null && etSearch.getText().toString().length() > 0) {
                            search();
                        }

                        return true;
                    }
                }
                return false;
            }
        });
    }

}
