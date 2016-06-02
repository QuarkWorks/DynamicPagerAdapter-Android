package com.quarkworks.dynamicviewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class DynamicPagerAdapter extends PagerAdapter {



    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
