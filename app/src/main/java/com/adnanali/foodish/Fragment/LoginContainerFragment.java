package com.adnanali.foodish.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adnanali.foodish.Adapter.LoginPagerAdapter;
import com.adnanali.foodish.Interface.ConnectorInterface;
import com.adnanali.foodish.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginContainerFragment extends Fragment {

//    private TabLayout tabLayout;
//    private ViewPager vpProdList;
//    private LoginPagerAdapter pagerAdapter;

    public LoginContainerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login_container, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        ViewPager vpProdList = (ViewPager) view.findViewById(R.id.vpProdList);
        LoginPagerAdapter pagerAdapter= new LoginPagerAdapter(getFragmentManager());

        vpProdList.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(vpProdList);


        if (getActivity() instanceof ConnectorInterface) {
            ((ConnectorInterface) getActivity()).changeTitle(getResources().getString(R.string.login));
        }
        return view;


    }



    private void setupPager(int currentItem){



    }




}
