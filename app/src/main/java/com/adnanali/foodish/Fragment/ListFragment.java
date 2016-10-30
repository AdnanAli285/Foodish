package com.adnanali.foodish.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adnanali.foodish.Adapter.GenericRecViewAdapter;
import com.adnanali.foodish.Enum.Type;
import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Interface.ConnectorInterface;
import com.adnanali.foodish.Model.ProductDetail;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Service.RestClient;
import com.adnanali.foodish.Utils.CommonHelper;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    RecyclerView rvList;
    private Context context;
    private boolean lastTypeIsBrand = false;
    private String title;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        if (title != null && context instanceof ConnectorInterface) {
            ((ConnectorInterface) context).changeTitle(title);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_list, container, false);
        rvList = (RecyclerView) view.findViewById(R.id.rvList);


        Bundle bundle = getArguments();
        if (title != null && context instanceof ConnectorInterface) {
            ((ConnectorInterface) context).changeTitle(title);
        }
        if (bundle != null) {
            String id = bundle.getString(CommonHelper.ID);
            getProducts(id);
        }


//            if (bundle != null) {
//                Type type = (Type) bundle.getSerializable(CommonHelper.TYPE);
//                String id = null;
//                if (type == Type.SubCategory || type == Type.Product) {
//                     id = bundle.getString(CommonHelper.ID);
//                     title = bundle.getString(CommonHelper.CAT);
//
//                    if (title != null && context instanceof ConnectorInterface) {
//                        ((ConnectorInterface) context).changeTitle(title);
//                    }
//
//                }
//                switch (type){
////                    case Category:
////                       // getSubCategories(id);
////                        break;
////                    case SubCategory:
////                        getSubCategories(id);
////                        break;
////                    case Brands:
////                        //getBrands();
////                        break;
//                    case Product:
//                        getProducts(id);
//                        break;
//                }
//            }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }




    private void getProducts(String id){
        final ProgressDialog dialog = ProgressDialog.show(context,"Getting Dishes",CommonHelper.DIALOG_MESSAGE);
        dialog.setCancelable(true);
            RestClient.getApi().GetDishes(id,new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    try {
                        dialog.dismiss();
//                        BaseModel[] list = CommonHelper.getGson().fromJson(jsonElement.toString(),ProductDetail[].class);
                        JSONArray jsonArray = new JSONArray(jsonElement.toString());
                        JSONArray dishes = jsonArray.getJSONObject(0).getJSONArray("dishes");
                        BaseModel[] list = CommonHelper.getGson().fromJson(dishes.toString(),ProductDetail[].class);

                        if (list != null ) {
                            showList(true,R.layout.cat_prod_row_item,list,Type.Product);
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

    private void showList(boolean isGrid, int layoutId, BaseModel[] list, Type type){
        GridLayoutManager glm = new GridLayoutManager(context,2, LinearLayoutManager.VERTICAL,false);
        rvList.setLayoutManager(glm);
        //isGrid
//        if (true) {
//
//        }else{
//            LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
//            rvList.setLayoutManager(llm);
//        }
        GenericRecViewAdapter adapter = new GenericRecViewAdapter(context,layoutId,list,type);
        rvList.setAdapter(adapter);
        //rvList.startAnimation(scaleAnimation);
    }

}
