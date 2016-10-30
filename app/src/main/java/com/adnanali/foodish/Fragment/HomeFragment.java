package com.adnanali.foodish.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adnanali.foodish.Activity.DrawerActivity;
import com.adnanali.foodish.Activity.MainActivity;
import com.adnanali.foodish.Adapter.GenericRecViewAdapter;
import com.adnanali.foodish.Adapter.SliderPagerAdapter;
import com.adnanali.foodish.Enum.DrawerType;
import com.adnanali.foodish.Enum.Type;
import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Interface.ConnectorInterface;
import com.adnanali.foodish.Model.ProductDetail;
import com.adnanali.foodish.Model.Restaurant;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Service.RestClient;
import com.adnanali.foodish.Utils.CommonHelper;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    EditText etSearch;
    private ViewPager vpHome;
    Context context;
    View view;
    Button btnMore,btnMoreRecent;
    RecyclerView rvNewArrival, rvRecent;
    RelativeLayout rlRecent;
    NestedScrollView nestedScrollView;
    private int limit = 50 * 1000;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (context instanceof ConnectorInterface) {
            ((ConnectorInterface) context).changeTitle(getResources().getString(R.string.app_name));
            setRecent();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        vpHome = (ViewPager) view.findViewById(R.id.pager);
        rlRecent = (RelativeLayout) view.findViewById(R.id.rlRecent);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.scrollView);

        FloatingActionButton fabSearch = (FloatingActionButton) view.findViewById(R.id.fabSearch);

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearch();
            }
        });

        init();
        setupBanners();
        if (context instanceof ConnectorInterface) {
            ((ConnectorInterface) context).changeTitle(getResources().getString(R.string.app_name));
        }
        //showList();
        getHomeProducts();
        setRecent();
        return view;
    }

    private void showList() {
        BaseModel[] list = new BaseModel[20];
        for (int i = 0; i < 20; i++) {
            list[i] =  (new Restaurant("id"+i , "name"+i , "description "+i ,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_wOx4dEQdjVnU066a7FpyVc5RfjR0fmtB2OyEcoa7ssAB5sob"
                    ,"town",24.9233123,67.0967195));
        }
        setNearest(list);
    }


    private void getHomeProducts(){
        final ProgressDialog dialog = ProgressDialog.show(context,"",CommonHelper.DIALOG_MESSAGE);
        RestClient.getApi().GetRestaurants(new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        try {
                            dialog.dismiss();
                            BaseModel[] list = CommonHelper.getGson().fromJson(jsonElement.toString(),Restaurant[].class);
                            if (list != null && list.length > 0) {
                                CommonHelper.setRestaurants(list);
                                setNearest(list);
                               // showList(true,R.layout.explore_grid_item,list,Type.Product);
                            }else{
                                Toast.makeText(context, "No Product found", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "No Product found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        dialog.dismiss();
                        Toast.makeText(context, "Internet problem", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setNearest(BaseModel[] list) {

        List<BaseModel> nearList = new ArrayList<>();

        for (BaseModel restaurant  : list) {

            if (((Restaurant)restaurant).getDistance() <= limit) {
                nearList.add(restaurant);
            }
        }

        GenericRecViewAdapter adapter = new GenericRecViewAdapter(context,R.layout.restaurant_item,nearList.toArray(new Restaurant[nearList.size()]),Type.Restaurants);
        adapter.notifyDataSetChanged();
        rvNewArrival.setAdapter(adapter);
    }

    private void setRecent() {

        List<BaseModel> list ;
        if (CommonHelper.getRecentList() != null && CommonHelper.getRecentList().size() > 0) {
            rlRecent.setVisibility(View.VISIBLE);

            if ( CommonHelper.getRecentList().size() > 10 ) {
                list =  CommonHelper.getRecentList().subList(0,9);
            }else{
                list = CommonHelper.getRecentList();
            }
            GenericRecViewAdapter adapter = new GenericRecViewAdapter(context,R.layout.cat_prod_row_item, list.toArray(new ProductDetail[list.size()]) ,Type.Product);
            adapter.notifyDataSetChanged();
            rvRecent.setAdapter(adapter);
        }else{
            rlRecent.setVisibility(View.GONE);
        }
    }




    private void setupBanners() {

            final SliderPagerAdapter adapter = new SliderPagerAdapter(context);
            vpHome.setAdapter(adapter);

        if (!CommonHelper.isTimerOn) {
            CommonHelper.isTimerOn = true;
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    ((MainActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (vpHome.getCurrentItem() == adapter.images.length - 1) {
                                vpHome.setCurrentItem(0);
                            } else {
                                vpHome.setCurrentItem(vpHome.getCurrentItem() + 1);
                            }
                        }
                    });

                }
            }, 2000, 3000);
        }
    }
    private void search() {
       Toast.makeText(context,"Not found",Toast.LENGTH_SHORT).show();
        RestClient.getApi().Search(etSearch.getText().toString(),
                new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }

    private void init(){
        btnMore = (Button) view.findViewById(R.id.btnMore);
        btnMoreRecent = (Button) view.findViewById(R.id.btnMoreRecent);
        rvNewArrival = (RecyclerView) view.findViewById(R.id.rvNewArrival);
        rvNewArrival.setNestedScrollingEnabled(false);
        rvRecent = (RecyclerView) view.findViewById(R.id.rvRecent);
        LinearLayoutManager llm = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        rvNewArrival.setLayoutManager(llm);

        LinearLayoutManager llm2 = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        rvRecent.setLayoutManager(llm2);

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnMoreRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openSearch();
                return true;
            }
        });
    }
    private void openSearch(){
        Intent intent = new Intent(context, DrawerActivity.class);
        intent.putExtra(CommonHelper.TYPE, DrawerType.SEARCH);
        startActivity(intent);
    }

}
