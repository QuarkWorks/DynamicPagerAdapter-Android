package com.quarkworks.dynamicviewpager_demo;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * A FrameLayout which is used in conjunction with a viewpager. This view allows touches to be
 * passed to the viewpager which are normally outside of the viewpager's bounds. Also has the
 * clip children false set to true so that views on the edge of the viewpager can be seen.
 * ("peeking views")
 * <p/>
 * Structured after https://gist.github.com/devunwired/8cbe094bb7a783e37ad1
 *
 * @author temple@hello.com (Benjamin Temple)
 */
public class PagerContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = PagerContainer.class.getSimpleName();

    private ViewPager pager;
    private boolean needsRedraw = false;

    public PagerContainer(Context context) {
        super(context);
        setupView();
    }

    public PagerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public PagerContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    private void setupView() {
        /**
         * Necessary to show views outside of the viewpager's frame.
         */
        setClipChildren(false);

        /**
         * Child clipping doesn't work with hardware acceleration in Android 3.x/4.x You need to
         * set this value here if using hardware acceleration in an application targeted at these
         * releases.
         */
        if (Build.VERSION.SDK_INT < 19) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    /**
     * Make sure that the child of this container is a viewpager if not, throw an exception.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        try {
            pager = (ViewPager) getChildAt(0);
            pager.setOnPageChangeListener(this);
        } catch (Exception e) {
            throw new IllegalStateException("The root child of PagerContainer must be a ViewPager");
        }
    }

    public ViewPager getViewPager() {
        return pager;
    }

    private Point center = new Point();
    private Point initialTouch = new Point();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        center.x = w / 2;
        center.y = h / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        /**
         * Capture any touches outside of the viewpager but within our container.
         */
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialTouch.x = (int) motionEvent.getX();
                initialTouch.y = (int) motionEvent.getY();
            default:
                motionEvent.offsetLocation(pager.getWidth()/2 - center.x, 0);
                break;
        }

        return pager.dispatchTouchEvent(motionEvent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /**
         * Force the container to redraw when scrolling.
         * Without this, the outer pages render initially but then stay static.
         */
        if (needsRedraw) invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        needsRedraw = (state != ViewPager.SCROLL_STATE_IDLE);
    }
}
