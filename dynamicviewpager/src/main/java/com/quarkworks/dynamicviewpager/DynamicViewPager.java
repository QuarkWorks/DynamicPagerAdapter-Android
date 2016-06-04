package com.quarkworks.dynamicviewpager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class DynamicViewPager extends ViewPager {
    private static final String TAG = DynamicViewPager.class.getSimpleName();

    private DynamicPagerAdapter dynamicPagerAdapter;

    private GestureDetector swipeGestureDetector;
    private SwipeGestureListener swipeGestureListener;

    public DynamicViewPager(Context context) {
        super(context);
        initialize();
    }

    public DynamicViewPager(Context context, AttributeSet attrs) {
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

    @Nullable public View getCurrentView() {
        return dynamicPagerAdapter.getViewAt(getCurrentItem());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        boolean handled = swipeGestureDetector.onTouchEvent(ev);

        if(!handled) {
//            if(ev.getActionMasked() == MotionEvent.ACTION_UP) {
//                if(swipeGestureListener.isScrolling()) {
//                    checkCurrentViewPosition();
//                    return true;
//                }
//            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private void checkCurrentViewPosition() {
        View currentView = getCurrentView();
        if(currentView == null) {
            return;
        }

        if(Math.abs(currentView.getTranslationY()) > 100) {
            //Do things
        }
    }

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
        public boolean onSingleTapUp(MotionEvent e) {
            isScrolling = false;
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            float difX = e2.getRawX() - e1.getRawX();
            float difY = e2.getRawY() - e1.getRawY();

            if(Math.abs(difX) > Math.abs(difY)) {
                return false;
            }

            View view = getCurrentView();
            if(view != null) {
                view.setTranslationY(initialTransY + difY);
                isScrolling = true;
            }

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            final View view = getCurrentView();
            if(view == null) {
                return false;
            }

            if(Math.abs(view.getTranslationY()) > 150 && Math.abs(velocityY) > Math.abs(velocityX)) {

                final float viewY = view.getY() + view.getTranslationX();
                final float toYDelta;

                if(velocityY < 0) {
                    toYDelta = -(viewY + view.getHeight());
                } else {
                    toYDelta = viewY + view.getHeight();
                }

                float duration = toYDelta / velocityY * 1000.0f;

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
            }
            return true;
        }

        public boolean isScrolling() {
            return isScrolling;
        }
    }
}
