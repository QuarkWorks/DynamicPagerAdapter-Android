package com.quarkworks.dynamicviewpager;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A PagerAdapter built to handle dynamic deletion of children with animations. Use discardView()
 * to start a dismissal animation. collapseViewsIn() is also exposed if you wish to use gestures
 * to delete items (this gets used in DynamicViewPager, for example).
 *
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
abstract public class DynamicPagerAdapter extends PagerAdapter {
    private static final String TAG = DynamicPagerAdapter.class.getSimpleName();

    public static final int POSITION_NOT_FOUND = -1;

    private WeakHashMap<Integer, View> children = new WeakHashMap<>();
    private boolean isChildAnimating = false;

    @Nullable private Callbacks callbacks;

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        View view = instantiateView(container, position);
        children.put(position, view);
        container.addView(view);
        return view;
    }

    @Override
    public final int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object view) {
        children.remove(position);
        container.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * Returns a View for the position provided if cached.
     *
     * A limited numbers of Views are cached (default is 5), so this is not too reliable except for
     * getting the current View or surrounding Views in the ViewPager.
     */
    @Nullable public View getViewAt(int position) {
        return children.get(position);
    }

    /**
     * Returns a position in the ViewPager for the View provided if cached.
     *
     * A limited numbers of Views are cached (default is 5), so this is not too reliable except for
     * getting the current View or surrounding Views in the ViewPager.
     */
    @Nullable public int getPositionForView(View view) {
        for(Map.Entry<Integer, View> entry : children.entrySet()) {
            if(entry.getValue().equals(view)) {
                return entry.getKey();

            }
        }
        return POSITION_NOT_FOUND;
    }

    /**
     * You may want to use this to stop gesture detection or other UI elements during animation.
     *
     * @return true if a child is in the middle of animating.
     */
    public boolean isChildAnimating() {
        return isChildAnimating;
    }

    /**
     * This is only used as a state holder. The DynamicPagerAdapter does not change behavior
     * due to this variable.
     */
    public void setChildAnimating(boolean childAnimating) {
        isChildAnimating = childAnimating;
    }

    /**
     * This will fade out the View passed in, then call collapseViewsIn() to finish the animation
     * (resembles a deletion).
     *
     * @param position The position of the View to hide and collapse around. You will usually want
     *                 to use the current item of the ViewPager.
     */
    public void discardViewAt(int position) {
        View view = getViewAt(position);
        discardView(view);
    }

    /**
     * This will fade out the View passed in, then call collapseViewsIn() to finish the animation
     * (resembles a deletion).
     *
     * @param view The view to hide and collapse around. You will usually want to use the current
     * item of the ViewPager.
     */
    public void discardView(final View view) {

//        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
//        alphaAnimation.setDuration(300);
//        alphaAnimation.setFillAfter(true);
//
//        alphaAnimation.setAnimationListener(new SimpleAnimationListener() {
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                collapseViewsIn(view);
//            }
//        });
//
//        view.startAnimation(alphaAnimation);

        RealTranslateAnimation translateAnimation = new RealTranslateAnimation(view, 0, 0, 0, -view.getHeight());
        translateAnimation.setDuration(400);
        translateAnimation.setFillAfter(true);
        translateAnimation.setInterpolator(new AccelerateInterpolator());

        translateAnimation.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                collapseViewsIn(view);
            }
        });

        view.startAnimation(translateAnimation);
        isChildAnimating = true;
    }

    /**
     * This will look for views to the right, then the left, to animate to the position of the View
     * passed in. This is used mainly for discardView(), but it is being exposed for gesture dismissals.
     *
     * @param view The view to collapse around. You will usually want to use the current item
     * of the ViewPager.
     */
    public void collapseViewsIn(final View view) {

        int position = getPositionForView(view);

        /**
         * Stop if a position can't be found
         */
        if(position == POSITION_NOT_FOUND) {
            if(callbacks != null) {
                isChildAnimating = false;
                callbacks.onDiscardFinished(position, view);
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
            if(callbacks != null) {
                isChildAnimating = false;
                callbacks.onDiscardFinished(position, view);
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

        final int pos = position;
        nextViewAnimation.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {

                /**
                 * Notify the callback on the next main loop (prevents screen flash)
                 */
                if(callbacks != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            isChildAnimating = false;
                            callbacks.onDiscardFinished(pos, view);
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

        isChildAnimating = true;
    }

    public abstract View instantiateView(ViewGroup container, int position);

    public interface Callbacks {
        void onDiscardFinished(int position, View view);
    }

    public void setCallbacks(@Nullable Callbacks callbacks) {
        this.callbacks = callbacks;
    }
}
