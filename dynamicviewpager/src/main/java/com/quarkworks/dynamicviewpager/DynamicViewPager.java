package com.quarkworks.dynamicviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class DynamicViewPager extends ViewPager {
    private static final String TAG = DynamicViewPager.class.getSimpleName();

    public DynamicViewPager(Context context) {
        super(context);
    }

    public DynamicViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
