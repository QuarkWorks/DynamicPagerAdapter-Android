package com.quarkworks.dynamicviewpager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import com.quarkworks.dynamicviewpager.helpers.RealTranslateAnimation;
import com.quarkworks.dynamicviewpager.helpers.SimpleAnimationListener;

/**
 * Listens for swipe and drag-and-drop gestures on children to delete items with animations.
 * Adapters *must* inherit from DynamicPagerAdapter for this to work, but DynamicPagerAdapter
 * can be used on its own if the use of gestures is not desired.
 *
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class SwipeRemovalViewPager extends ViewPager {
    private static final String TAG = SwipeRemovalViewPager.class.getSimpleName();

    private DynamicPagerAdapter dynamicPagerAdapter;

    private GestureDetector swipeGestureDetector;
    private SwipeGestureListener swipeGestureListener;

    public SwipeRemovalViewPager(Context context) {
        super(context);
        initialize();
    }

    public SwipeRemovalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        swipeGestureListener = new SwipeGestureListener();
        swipeGestureDetector = new GestureDetector(getContext(), swipeGestureListener);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if(!(adapter instanceof DynamicPagerAdapter)) {
            throw new IllegalStateException("You must use a DynamicPagerAdapter along with the " +
                    "DynamicViewPager.");
        }

        dynamicPagerAdapter = (DynamicPagerAdapter) adapter;
        super.setAdapter(adapter);
    }

    public DynamicPagerAdapter getDynamicPagerAdapter() {
        return dynamicPagerAdapter;
    }

    @Nullable public View getCurrentView() {
        return dynamicPagerAdapter.getViewAt(getCurrentItem());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(dynamicPagerAdapter.isViewAnimating()) {
            return true;
        }

        boolean handled = swipeGestureDetector.onTouchEvent(ev);

        if(!handled) {
            if(ev.getActionMasked() == MotionEvent.ACTION_UP) {
                final View view = getCurrentView();

                if(view != null && (view.getAnimation() == null || view.getAnimation().hasEnded()) && swipeGestureListener.isScrolling()) {

                    final float toYDelta;
                    final boolean deleting;

                    final float screenHeight = getRootView().getHeight();
                    final float dropBarrier = screenHeight * 0.2f;

                    if(Math.abs(view.getTranslationY()) > dropBarrier) {
                        deleting = true;

                        final float actualY = view.getY() + view.getTranslationY();

                        if(view.getTranslationY() < 0) {
                            toYDelta = -view.getHeight();
                        } else {
                            toYDelta = actualY + view.getHeight();
                        }
                    } else {
                        deleting = false;
                        toYDelta = -view.getTranslationY();
                    }

                    RealTranslateAnimation translateAnimation = new RealTranslateAnimation(view, 0, 0, 0, toYDelta);
                    translateAnimation.setDuration(400);
                    translateAnimation.setFillAfter(true);
                    translateAnimation.setInterpolator(new AccelerateInterpolator());

                    translateAnimation.setAnimationListener(new SimpleAnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if(deleting) {
                                dynamicPagerAdapter.collapseViewsIn(view);
                            } else {
                                dynamicPagerAdapter.setViewAnimating(false);
                            }
                        }
                    });

                    view.startAnimation(translateAnimation);
                    dynamicPagerAdapter.setViewAnimating(true);
                }
            }
        }

        return swipeGestureListener.isScrolling() || dynamicPagerAdapter.isViewAnimating() || super.dispatchTouchEvent(ev);
    }

    /**
     * A GestureListener for swipe gestures
     */
    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

        private float initialTransY = 0;
        private boolean isScrolling = false;

        @Override
        public boolean onDown(MotionEvent e) {
            View view = getCurrentView();
            initialTransY = view == null ? 0 : view.getTranslationY();

            isScrolling = false;
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            float difX = e2.getRawX() - e1.getRawX();
            float difY = e2.getRawY() - e1.getRawY();

            double rad = Math.tanh((double)(difX / difY));
            double deg = Math.toDegrees(rad);

            if(Math.abs(deg) > 30.0) {
                return false;
            }

            View view = getCurrentView();
            if(view != null) {
                view.setTranslationY(initialTransY + difY);
                isScrolling = true;
                return true;
            }

            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            final View view = getCurrentView();
            if(view == null) {
                return false;
            }

            double rad = Math.tanh((double)(velocityX / velocityY));
            double deg = Math.toDegrees(rad);

            if(Math.abs(view.getTranslationY()) > 150 && Math.abs(deg) <= 30.0 && Math.abs(velocityY) > 500.0f) {

                final float screenHeight = getRootView().getHeight();
                final float toYDelta;

                final float flingSpeedBarrier = screenHeight * 2;

                if(velocityY < -flingSpeedBarrier) {
                    toYDelta = -screenHeight * 2;

                } else if(velocityY > flingSpeedBarrier) {
                    toYDelta = screenHeight * 2;

                } else {
                    return false;
                }

                float duration = toYDelta / velocityY * 1000.0f;

                if(duration > 400.0f) {
                    duration = 400.0f;
                }

                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, toYDelta);
                translateAnimation.setDuration((long)duration);
                translateAnimation.setFillAfter(true);
                translateAnimation.setInterpolator(new DecelerateInterpolator());

                translateAnimation.setAnimationListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dynamicPagerAdapter.collapseViewsIn(view);
                    }
                });

                view.startAnimation(translateAnimation);
                dynamicPagerAdapter.setViewAnimating(true);

                return true;
            }

            return false;
        }

        public boolean isScrolling() {
            return isScrolling;
        }
    }
}
