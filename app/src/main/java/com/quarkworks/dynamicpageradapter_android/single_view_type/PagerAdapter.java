package com.quarkworks.dynamicpageradapter_android.single_view_type;

import android.view.View;
import android.view.ViewGroup;

import com.quarkworks.dynamicviewpager.DynamicPagerAdapter;

import java.util.ArrayList;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class PagerAdapter extends DynamicPagerAdapter {
    private static final String TAG = PagerAdapter.class.getSimpleName();

    private ArrayList<Integer> values = new ArrayList<>();

    public PagerAdapter() {
        for(int i = 0; i < 30; i++) {
            values.add(i);
        }

        setCallbacks(new Callbacks() {
            @Override
            public void onDiscardFinished(int position, View view) {
                if(position != NO_POSITION) {
                    values.remove(position);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int position, int viewType) {
        final PagerCardView pagerCardView = new PagerCardView(container.getContext());

        pagerCardView.setPagerViewCallbacks(new PagerCardView.PagerViewCallbacks() {
            @Override
            public void dismissClicked(int position) {
                discardView(pagerCardView);
            }
        });

        return new ViewHolder(pagerCardView) {};
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        PagerCardView pagerCardView = (PagerCardView) viewHolder.view;

        pagerCardView.setViewData(values.get(position));
    }

    @Override
    public int getCount() {
        return values.size();
    }
}
