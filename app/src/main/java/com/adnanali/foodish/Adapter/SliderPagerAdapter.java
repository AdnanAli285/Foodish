package com.adnanali.foodish.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.adnanali.foodish.R;


/**
 * Created by Adnan Ali on 6/1/2016.
 */
public class SliderPagerAdapter extends PagerAdapter {


    int width ;
    int height;
    private Context context;
  //  private int[] images = new int[]{R.mipmap.banner1,R.mipmap.cloth3,R.mipmap.banner2,R.mipmap.gift11,R.mipmap.banner3};
    public int[] images = new int[]{R.mipmap.i1, R.mipmap.i2,R.mipmap.i3,R.mipmap.i4};
//    public int[] images = new int[]{0};
    LayoutInflater layoutInflater;
    public SliderPagerAdapter(Context context){
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = (int) (width * 0.6837);
    }

    @Override
    public int getCount() {
       // return sliders != null ? sliders.length : 4;
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
       return view == (ImageView) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = (ImageView) layoutInflater.inflate(R.layout.pager_layout,container,false);
        view.setBackgroundResource(images[position]);
        container.addView(view);
        return view;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView)object);
    }
}
