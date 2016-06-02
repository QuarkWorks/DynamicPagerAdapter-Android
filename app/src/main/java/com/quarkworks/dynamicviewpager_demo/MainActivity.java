package com.quarkworks.dynamicviewpager_demo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager_id);

        viewPager.setPageMarginDrawable(null);
        viewPager.setPageMargin(30);
        viewPager.setOffscreenPageLimit(2);


        DemoPagerAdapter pagerAdapter = new DemoPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
    }
}
