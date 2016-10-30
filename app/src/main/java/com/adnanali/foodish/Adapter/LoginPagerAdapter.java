package com.adnanali.foodish.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.adnanali.foodish.Fragment.LoginFragment;
import com.adnanali.foodish.Fragment.RegisterFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adnan Ali on 10/13/2016.
 */

public class LoginPagerAdapter extends FragmentStatePagerAdapter {


    private List<String> names ;

    public LoginPagerAdapter(FragmentManager fm) {

        super(fm);
        names = new ArrayList<>();
        names.add("LOGIN");
        names.add("CREATE ACCOUNT");
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new LoginFragment();
        } else if (position == 1) {
            return new RegisterFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return names != null ? names.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return names.get(position);
    }
}
