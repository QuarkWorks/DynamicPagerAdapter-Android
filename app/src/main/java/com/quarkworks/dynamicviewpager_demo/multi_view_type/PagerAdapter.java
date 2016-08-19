package com.quarkworks.dynamicviewpager_demo.multi_view_type;

import android.view.View;
import android.view.ViewGroup;

import com.quarkworks.dynamicviewpager.DynamicPagerAdapter;

import java.util.ArrayList;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class PagerAdapter extends DynamicPagerAdapter {
    private static final String TAG = PagerAdapter.class.getSimpleName();

    public static final class ViewTypes {
        public static final int BLUE = 0;
        public static final int GREEN = 1;
        public static final int RED = 2;
    }

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
    public ViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        final View view;

        if (viewType == ViewTypes.BLUE) {
            view = new BlueCardView(container.getContext());

            ((BlueCardView) view).setPagerViewCallbacks(new BlueCardView.PagerViewCallbacks() {
                @Override
                public void dismissClicked(int position) {
                    discardView(view);
                }
            });

        } else if (viewType == ViewTypes.GREEN) {
            view = new GreenCardView(container.getContext());

            ((GreenCardView) view).setPagerViewCallbacks(new GreenCardView.PagerViewCallbacks() {
                @Override
                public void dismissClicked(int position) {
                    discardView(view);
                }
            });

        } else {
            view = new RedCardView(container.getContext());

            ((RedCardView) view).setPagerViewCallbacks(new RedCardView.PagerViewCallbacks() {
                @Override
                public void dismissClicked(int position) {
                    discardView(view);
                }
            });
        }

        return new ViewHolder(view) {};
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        int value = values.get(position);

        if (viewHolder.viewType == ViewTypes.BLUE) {
            ((BlueCardView) viewHolder.view).setViewData(value);

        } else if (viewHolder.viewType == ViewTypes.GREEN) {
            ((GreenCardView) viewHolder.view).setViewData(value);

        } else {
            ((RedCardView) viewHolder.view).setViewData(value);
        }
    }

    @Override
    public int getViewType(int position) {
        return values.get(position) % 3;
    }

    @Override
    public int getCount() {
        return values.size();
    }
}
