package com.quarkworks.dynamicviewpager_demo;

import android.view.View;
import android.view.ViewGroup;

import com.quarkworks.dynamicviewpager.DynamicPagerAdapter;

import java.util.ArrayList;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class DemoPagerAdapter extends DynamicPagerAdapter {

    private ArrayList<Integer> values = new ArrayList<>();

    public DemoPagerAdapter() {
        for(int i = 0; i < 30; i++) {
            values.add(i);
        }
    }

    @Override
    public View instantiateView(ViewGroup container, int position) {
        final PagerView pagerView = new PagerView(container.getContext());

        pagerView.setViewData(values.get(position));

        pagerView.setPagerViewCallbacks(new PagerView.PagerViewCallbacks() {
            @Override
            public void dismissClicked(final int position) {

                discardView(pagerView, new OnDiscardFinishedCallback() {
                    @Override
                    public void onDiscardFinished() {
                        values.remove(Integer.valueOf(position));
                        notifyDataSetChanged();
                    }
                });
            }
        });

        return pagerView;
    }

    @Override
    public int getCount() {
        return values.size();
    }
}
