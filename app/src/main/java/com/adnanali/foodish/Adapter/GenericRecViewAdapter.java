package com.adnanali.foodish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adnanali.foodish.Activity.DrawerActivity;
import com.adnanali.foodish.Activity.MainActivity;
import com.adnanali.foodish.Activity.ProductDetailActivity;
import com.adnanali.foodish.Enum.Type;
import com.adnanali.foodish.Fragment.ListFragment;
import com.adnanali.foodish.Interface.BaseModel;
import com.adnanali.foodish.Interface.ConnectorInterface;
import com.adnanali.foodish.Model.ProductDetail;
import com.adnanali.foodish.Model.Restaurant;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Service.RestClient;
import com.adnanali.foodish.Utils.CommonHelper;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Adnan Ali on 4/21/2016.
 */
public class GenericRecViewAdapter extends RecyclerView.Adapter<GenericRecViewAdapter.Holder> {


    public static final int LIMIT = 10;
    private final DecimalFormat formatter;
    Context context;
    BaseModel[] list;
    int layoutResourceId;
    Type type;
    int height;
    int width;
    public GenericRecViewAdapter(Context context , int layoutResourceId , BaseModel[] list, Type type){
        this.context = context;
        this.list = list;
        this.layoutResourceId = layoutResourceId;
        this.type = type;
        formatter = new DecimalFormat("#,###,###");
        if (type == Type.Product || type == Type.Restaurants) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
             height = displayMetrics.heightPixels;
             width = displayMetrics.widthPixels;
             //width = width - (int) (3 * displayMetrics.density + 0.5); // 3 dp is padding
        }

    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //R.layout.cat_prod_row_item and

        View itemView = LayoutInflater.from(context).inflate(this.layoutResourceId,parent,false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.name.setText(getItem(position).getName());
        Picasso.with(context)
                .load(getItem(position).getImage())
                .error(R.drawable.logo)
                //.placeholder(R.drawable.prod_place_holder)
                .into(holder.image);
        if (type == Type.Product) {

            try {
//                holder.price.setText(ProductDetail.CURRENCY_UNIT + formatter.format(Long.valueOf( (String) getItem(position).getPrice())));
                holder.price.setText(ProductDetail.CURRENCY_UNIT +  getItem(position).getPrice());
            } catch (Exception e) {
                e.printStackTrace();
                holder.price.setText(ProductDetail.CURRENCY_UNIT + getItem(position).getPrice());
            }
       }

        if (type == Type.Restaurants) {
            Restaurant restaurant = (Restaurant) getItem(position);
            holder.tvArea.setText(restaurant.getTown());
            holder.tvDesc.setText(restaurant.getDesc());
            if (restaurant.getReviews() != null) {
                holder.tvRating.setText("Rating: "+ restaurant.getReviews().getRating());
            }
            if (CommonHelper.getLatLng() != null) {
                holder.tvDistance.setText((restaurant.getDistance())/1000 + " Km away");
            }

            holder.ivRating.setRating(restaurant.getRating());
        }
    }


    private BaseModel getItem(int position){
        return list[position];
    }



    @Override
    public int getItemCount() {
        return list != null ? list.length : 0;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener , RatingBar.OnRatingBarChangeListener{
        ImageView image;
        TextView name,price,tvDesc,tvArea,tvDistance,tvRating;
        RatingBar ivRating;
        public Holder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvName);
            image = (ImageView) itemView.findViewById(R.id.image);
            if (type == Type.Product || type == Type.Restaurants) {

                if (type == Type.Product) {
                    price = (TextView) itemView.findViewById(R.id.tvPrice);
                    price.setVisibility(View.VISIBLE);
                }

                if (layoutResourceId == R.layout.cat_prod_row_item) {
                    CardView container = (CardView) itemView.findViewById(R.id.container);
                    setLayoutWidth(container);
                }

            }
            if (type == Type.Restaurants) {
                ivRating = (RatingBar) itemView.findViewById(R.id.ivRating);
                tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
                tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
                tvArea = (TextView) itemView.findViewById(R.id.tvArea);
                tvRating = (TextView) itemView.findViewById(R.id.tvRating);
                ivRating.setOnRatingBarChangeListener(this);
            }


            itemView.setOnClickListener(this);
        }

        private void setLayoutWidth(CardView container) {
            if (container != null) {
                int containerWidth = (48*width)/100;
                container.getLayoutParams().width = containerWidth;
            }
        }

        private ArrayList<String> getProductIds(){
            ArrayList<String> ids = new ArrayList<>();
            for (int i = 0 ; i < list.length ; i++) {
                ids.add(list[i].getId());
            }
            return ids;
        }

        private ArrayList<String> getProductJson(){
            ArrayList<String> ids = new ArrayList<>();
            for (int i = 0 ; i < list.length ; i++) {
                ids.add(CommonHelper.getGson().toJson(list[i]));
            }
            return ids;
        }



        @Override
        public void onClick(View v) {
           // Toast.makeText(context, "lf;sjdf", Toast.LENGTH_SHORT).show();
            BaseModel model = getItem(getAdapterPosition());
           // item click
                if (list != null && list.length > 0) {

                    if (context instanceof ConnectorInterface) {
                        ((ConnectorInterface) context).changeTitle(model.getName());
                    }
                    switch (type){

                        case Restaurants:
                            if (context != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString(CommonHelper.ID,model.getId());
                                ListFragment fragment = new ListFragment();
                                fragment.setArguments(bundle);
                                if (context instanceof MainActivity) {
                                    ((MainActivity) context).getSupportFragmentManager().
                                            beginTransaction().replace(R.id.fragmentContainer,fragment)
                                            .addToBackStack(null).commit();
                                }else{
                                    ((DrawerActivity) context).getSupportFragmentManager().
                                            beginTransaction().replace(R.id.fragmentContainer,fragment)
                                            .addToBackStack(null).commit();
                                }


//                                if(((Restaurant)getItem(getAdapterPosition())).getChildCategories() == null){
//                                    Intent intent = new Intent(context,ListActivity.class);
//                                    intent.putExtra(CommonHelper.TYPE,Type.Product);
//                                    intent.putExtra(CommonHelper.ID,getItem(getAdapterPosition()).getId());
//                                    intent.putExtra(CommonHelper.CAT,getItem(getAdapterPosition()).getName());
//                                    context.startActivity(intent);
//                                }
//                                else {
//                                    ((ConnectorInterface) context).changeFragment(true,new MainFragment(),
//                                            new CustomDictionary(CommonHelper.ID, getItem(getAdapterPosition()).getId()));
//                                }
                            }

                            break;

                        case Product:
                            CommonHelper.addToRecent(list[getAdapterPosition()]);
                            Intent intent = new Intent(context,ProductDetailActivity.class);
//                            intent.putStringArrayListExtra(ProductDetailActivity.PRODUCT_JSON,getProductIds());
                            intent.putStringArrayListExtra(ProductDetailActivity.PRODUCT_JSON, getProductJson());
                            intent.putExtra(ProductDetailActivity.CURRENT_PRODUCT_INDEX,getAdapterPosition());
                            context.startActivity(intent);
                            break;
                    }
                }
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            rate(list[getAdapterPosition()].getId(),(int)v);
        }
    }


    private void rate(String id,int rating){
        RestClient.getApi().RateRestaurant(id, rating, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
