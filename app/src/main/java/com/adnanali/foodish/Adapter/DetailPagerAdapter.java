package com.adnanali.foodish.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.adnanali.foodish.Fragment.ProductDetailFragment;

import java.util.List;

/**
 * Created by Adnan Ali on 6/7/2016.
 */
public class DetailPagerAdapter extends FragmentStatePagerAdapter {

    List<String> productJson;

    public DetailPagerAdapter(FragmentManager fm, List<String> productIds) {
        super(fm);
        this.productJson = productIds;
    }


    @Override
    public Fragment getItem(int position) {
       return ProductDetailFragment.getNewInstance( productJson.get(position));
    }

    @Override
    public int getCount() {
        return productJson != null ? productJson.size() : 0;
    }
}
