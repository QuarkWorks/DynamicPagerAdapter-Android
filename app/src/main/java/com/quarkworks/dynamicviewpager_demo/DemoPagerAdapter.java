package com.quarkworks.dynamicviewpager_demo;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class DemoPagerAdapter extends PagerAdapter {
    private static final String TAG = DemoPagerAdapter.class.getSimpleName();

    private ArrayList<Integer> positions = new ArrayList<>();

    public DemoPagerAdapter() {
        for(int i = 0; i < 30; i++) {
            positions.add(i);
        }
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        final PagerView pagerView = new PagerView(container.getContext());

        pagerView.setViewData(positions.get(position));

        pagerView.setPagerViewCallbacks(new PagerView.PagerViewCallbacks() {
            @Override
            public void dismissClicked(int position) {
                positions.remove(position);
                notifyDataSetChanged();
            }
        });

        container.addView(pagerView);
        return pagerView;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public int getCount() {
        return positions.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
