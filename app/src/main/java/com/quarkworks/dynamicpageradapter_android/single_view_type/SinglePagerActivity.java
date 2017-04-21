package com.quarkworks.dynamicpageradapter_android.single_view_type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.quarkworks.dynamicpageradapter_android.R;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class SinglePagerActivity extends AppCompatActivity {
    private static final String TAG = SinglePagerActivity.class.getSimpleName();

    public static Intent newIntent(Context context) {
        return new Intent(context, SinglePagerActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_activity);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_activity_view_pager);

        viewPager.setPageMarginDrawable(null);
        viewPager.setPageMargin(30);
        viewPager.setOffscreenPageLimit(2);


        PagerAdapter pagerAdapter = new PagerAdapter();
        viewPager.setAdapter(pagerAdapter);
    }
}
