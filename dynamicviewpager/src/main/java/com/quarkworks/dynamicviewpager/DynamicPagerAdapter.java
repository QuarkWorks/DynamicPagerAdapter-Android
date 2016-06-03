package com.quarkworks.dynamicviewpager;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
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

    /**
     * This will fade out the View passed in, then call collapseViewsIn() to finish the animation
     * (resembles a deletion).
     *
     * @param view The view to hide and collapse around. You will usually want to use the current
     * item of the ViewPager.
     * @param onDiscardFinishedCallback This gets called when the animations have finished. If you
     * are deleting an item, you should remove it from your data set and call notifyDataSetChanged().
     */
    public void discardView(final View view, @Nullable final OnDiscardFinishedCallback onDiscardFinishedCallback) {

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);

        alphaAnimation.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                collapseViewsIn(view, onDiscardFinishedCallback);
            }
        });

        view.startAnimation(alphaAnimation);
    }

    /**
     * This will look for views to the right, then the left, to animate to the position of the View
     * passed in. This is used mainly for discardView(), but it is being exposed for gesture dismissals.
     *
     * @param view The view to collapse around. You will usually want to use the current item
     * of the ViewPager.
     * @param onDiscardFinishedCallback This gets called when the animations have finished. If you
     * are deleting an item, you should remove it from your data set and call notifyDataSetChanged().
     */
    public void collapseViewsIn(View view, @Nullable final OnDiscardFinishedCallback onDiscardFinishedCallback) {

        /**
         * Get position for current View
         */
        int position = -1;

        for(Map.Entry<Integer, View> entry : children.entrySet()) {
            if(entry.getValue().equals(view)) {
                position = entry.getKey();
            }
        }

        /**
         * Stop if a position can't be found
         */
        if(position == -1) {
            if(onDiscardFinishedCallback != null) {
                onDiscardFinishedCallback.onDiscardFinished();
            }
            return;
        }

        /**
         * Get next view (check right first, then left)
         */
        View nextView = children.get(position+1);
        View farNextView = children.get(position+2);

        if(nextView == null) {
            nextView = children.get(position-1);
            farNextView = children.get(position-2);
        }

        /**
         * If it is still null, just discard the current
         */
        if(nextView == null) {
            if(onDiscardFinishedCallback != null) {
                onDiscardFinishedCallback.onDiscardFinished();
            }
            return;
        }

        /**
         * Start collapsing animations
         */
        float toXDelta = view.getX() - nextView.getX();

        TranslateAnimation nextViewAnimation = new TranslateAnimation(0, toXDelta, 0, 0);
        nextViewAnimation.setDuration(400);
        nextViewAnimation.setFillAfter(true);

        nextViewAnimation.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {

                /**
                 * Notify the callback on the next main loop (prevents screen flash)
                 */
                if(onDiscardFinishedCallback != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            onDiscardFinishedCallback.onDiscardFinished();
                        }
                    });
                }
            }
        });

        TranslateAnimation farNextViewAnimation = new TranslateAnimation(0, toXDelta, 0, 0);
        farNextViewAnimation.setDuration(400);
        farNextViewAnimation.setFillAfter(true);

        nextView.startAnimation(nextViewAnimation);

        if(farNextView != null) {
            farNextView.startAnimation(farNextViewAnimation);
        }
    }

    public abstract View instantiateView(ViewGroup container, int position);

    public interface OnDiscardFinishedCallback {
        void onDiscardFinished();
    }
}
