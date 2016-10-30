package com.adnanali.foodish.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.adnanali.foodish.Enum.DrawerType;
import com.adnanali.foodish.Fragment.AboutUsFragment;
import com.adnanali.foodish.Fragment.FeedbackFragment;
import com.adnanali.foodish.Fragment.OrderHistoryFragment;
import com.adnanali.foodish.Fragment.SearchFragment;
import com.adnanali.foodish.R;
import com.adnanali.foodish.Utils.CommonHelper;


public class DrawerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        settingToolbar();
        DrawerType type = DrawerType.NOTHING;
        if (getIntent().hasExtra(CommonHelper.TYPE)) {
            type = (DrawerType) getIntent().getSerializableExtra(CommonHelper.TYPE);
        }




        Fragment fragment = null;
        switch (type){
            case SEARCH:
                fragment = new SearchFragment();
                break;

            case ORDER_HISTORY:
                fragment = new OrderHistoryFragment();
                break;

            case ABOUT_US:
                fragment = new AboutUsFragment();
                break;

            case ACCOUNT:

                break;

            case FEEDBACK:
                fragment = new FeedbackFragment();
                break;


        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,fragment)
                    .commit();
        }

    }

    private void settingToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Navigation");
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.pager_replacer));
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
