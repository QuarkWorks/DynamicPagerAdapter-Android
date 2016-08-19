package com.quarkworks.dynamicviewpager_demo.multi_view_type;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quarkworks.dynamicviewpager_demo.R;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class GreenCardView extends RelativeLayout {
    private static final String TAG = GreenCardView.class.getSimpleName();

    private int position = 0;

    private TextView textView;

    @Nullable
    private PagerViewCallbacks pagerViewCallbacks;

    public GreenCardView(Context context) {
        super(context);
        initialize();
    }

    public GreenCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public GreenCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.pager_card_view, this);

        View cardContainer = findViewById(R.id.pager_card_view_card_container);
        textView = (TextView) findViewById(R.id.pager_card_view_text);
        View dismissButton = findViewById(R.id.pager_card_view_dismiss_button);

        dismissButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pagerViewCallbacks != null) {
                    pagerViewCallbacks.dismissClicked(position);
                }
            }
        });

        cardContainer.setBackgroundColor(Color.GREEN);
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
