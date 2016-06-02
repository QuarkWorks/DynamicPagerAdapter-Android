package com.quarkworks.dynamicviewpager_demo;

import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.quarkworks.dynamicviewpager.SimpleAnimationListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class DemoPagerAdapter extends PagerAdapter {
    private static final String TAG = DemoPagerAdapter.class.getSimpleName();

    private HashMap<Integer, View> children = new HashMap<>();
    private ArrayMap<Integer, View> map = new ArrayMap<>();

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
            public void dismissClicked(final int position) {

                AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
                animation.setDuration(300);
                animation.setFillAfter(true);

                animation.setAnimationListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {

                        int index = container.indexOfChild(pagerView);
                        PagerView rightView = (PagerView) children.get((index+1));

                        if(rightView != null) {

                            Log.d(TAG, "current: " + pagerView.getPosition());
                            Log.d(TAG, "right: " + rightView.getPosition());

                            float toXDelta = pagerView.getX() - rightView.getX();

                            TranslateAnimation translateAnimation = new TranslateAnimation(0, toXDelta, 0, 0);
                            translateAnimation.setDuration(400);
                            translateAnimation.setFillAfter(true);

                            translateAnimation.setAnimationListener(new SimpleAnimationListener() {
                                @Override
                                public void onAnimationEnd(Animation animation) {

                                    if(positions.contains(position)) {
                                        positions.remove(Integer.valueOf(position));
                                        notifyDataSetChanged();
                                    }
                                }
                            });

                            rightView.startAnimation(translateAnimation);

//                            if(farRightView != null) {
//                                farRightView.startAnimation(translateAnimation);
//                            }
                        }
                    }
                });

                pagerView.startAnimation(animation);
            }
        });

        container.addView(pagerView);
        children.put(position, pagerView);
        return pagerView;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
        children.remove(position);
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
