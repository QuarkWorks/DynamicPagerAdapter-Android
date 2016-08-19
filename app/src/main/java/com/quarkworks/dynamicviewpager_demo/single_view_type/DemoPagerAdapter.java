package com.quarkworks.dynamicviewpager_demo.single_view_type;

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
    public ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        final PagerView pagerView = new PagerView(container.getContext());

        pagerView.setPagerViewCallbacks(new PagerView.PagerViewCallbacks() {
            @Override
            public void dismissClicked(int position) {
                discardView(pagerView);
            }
        });

        return new ViewHolder(pagerView) {};
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        PagerView pagerView = (PagerView) viewHolder.view;

        pagerView.setViewData(values.get(position));
    }

    @Override
    public int getCount() {
        return values.size();
    }
}
