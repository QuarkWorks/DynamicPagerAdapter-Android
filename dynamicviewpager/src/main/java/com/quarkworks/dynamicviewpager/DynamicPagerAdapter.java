package com.quarkworks.dynamicviewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
abstract public class DynamicPagerAdapter extends PagerAdapter {
    private static final String TAG = DynamicPagerAdapter.class.getSimpleName();

    private WeakHashMap<Integer, View> children = new WeakHashMap<>();

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = instantiateView(container, position);
        children.put(position, view);
        container.addView(view);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        children.remove(position);
        container.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void discardView(final View view, final OnDiscardFinishedCallback onDiscardFinishedCallback) {

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);

        alphaAnimation.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {

                /**
                 * Get position for current View
                 */
                int position = -1;

                for(Map.Entry<Integer, View> entry : children.entrySet()) {
                    if(entry.getValue().equals(view)) {
                        position = entry.getKey();
                    }
                }

                if(position == -1) {
                    onDiscardFinishedCallback.onDiscardFinished();
                    return;
                }

                /**
                 * Get next view (check right first, then left)
                 */
                View nextView = children.get(position+1);
                if(nextView == null) {
                    nextView = children.get(position-1);
                }

                /**
                 * If it is still null, just discard the current
                 */
                if(nextView == null) {
                    onDiscardFinishedCallback.onDiscardFinished();
                    return;
                }

                /**
                 * Animate the next card in
                 */
                float toXDelta = view.getX() - nextView.getX();

                TranslateAnimation translateAnimation = new TranslateAnimation(0, toXDelta, 0, 0);
                translateAnimation.setDuration(400);
                translateAnimation.setFillAfter(true);

                translateAnimation.setAnimationListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        onDiscardFinishedCallback.onDiscardFinished();
                    }
                });

                nextView.startAnimation(translateAnimation);
            }
        });

        view.startAnimation(alphaAnimation);
    }

    public abstract View instantiateView(ViewGroup container, int position);

    public interface OnDiscardFinishedCallback {
        void onDiscardFinished();
    }
}
