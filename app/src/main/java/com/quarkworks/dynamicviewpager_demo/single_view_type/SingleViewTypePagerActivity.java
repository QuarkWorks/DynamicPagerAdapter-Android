package com.quarkworks.dynamicviewpager_demo.single_view_type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.quarkworks.dynamicviewpager_demo.R;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class SingleViewTypePagerActivity extends AppCompatActivity {
    private static final String TAG = SingleViewTypePagerActivity.class.getSimpleName();

    public static Intent newIntent(Context context) {
        return new Intent(context, SingleViewTypePagerActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_pager_activity);

        ViewPager viewPager = (ViewPager) findViewById(R.id.simple_pager_activity_view_pager);

        viewPager.setPageMarginDrawable(null);
        viewPager.setPageMargin(30);
        viewPager.setOffscreenPageLimit(2);


        DemoPagerAdapter pagerAdapter = new DemoPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
    }
}
