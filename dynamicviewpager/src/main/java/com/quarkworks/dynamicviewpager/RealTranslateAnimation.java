package com.quarkworks.dynamicviewpager;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class RealTranslateAnimation extends Animation {
    private static final String TAG = RealTranslateAnimation.class.getSimpleName();

    private View view;
    private float fromXDelta, toXDelta = -1.0f;
    private float fromYDelta, toYDelta = -1.0f;

    private float initialTransX = -1.0f;
    private float initialTransY = -1.0f;

    public RealTranslateAnimation(View view, float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        this.view = view;
        this.fromXDelta = fromXDelta;
        this.toXDelta = toXDelta;
        this.fromYDelta = fromYDelta;
        this.toYDelta = toYDelta;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if(initialTransX == -1.0f) {
            initialTransX = view.getTranslationX();
            initialTransY = view.getTranslationY();
        }

        float offsetX = (toXDelta - fromXDelta) * interpolatedTime;
        float offsetY = (toYDelta - fromYDelta) * interpolatedTime;

        float transX = initialTransX + offsetX;
        float transY = initialTransY + offsetY;

        view.setTranslationX(transX);
        view.setTranslationY(transY);
    }
}
