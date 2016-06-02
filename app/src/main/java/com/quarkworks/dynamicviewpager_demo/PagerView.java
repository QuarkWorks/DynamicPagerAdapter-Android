package com.quarkworks.dynamicviewpager_demo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class PagerView extends RelativeLayout {
    private static final String TAG = PagerView.class.getSimpleName();

    private int position = 0;

    private TextView textView;

    @Nullable private PagerViewCallbacks pagerViewCallbacks;

    public PagerView(Context context) {
        super(context);
        initialize();
    }

    public PagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public PagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.pager_view, this);

        textView = (TextView) findViewById(R.id.pager_view_text_id);
        View dismissButton = findViewById(R.id.pager_view_dismiss_button_id);

        dismissButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pagerViewCallbacks != null) {
                    pagerViewCallbacks.dismissClicked(position);
                }
            }
        });
    }

    public void setViewData(int position) {
        this.position = position;
        textView.setText(String.valueOf(position));
    }

    public int getPosition() {
        return position;
    }

    public void setPagerViewCallbacks(@Nullable PagerViewCallbacks pagerViewCallbacks) {
        this.pagerViewCallbacks = pagerViewCallbacks;
    }

    public interface PagerViewCallbacks {
        void dismissClicked(int position);
    }
}
