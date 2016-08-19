package com.quarkworks.dynamicviewpager_demo.multi_view_type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author jacobamuchow@gmail.com (Jacob Muchow)
 */
public class MultiViewTypePagerActivity extends AppCompatActivity {
    private static final String TAG = MultiViewTypePagerActivity.class.getSimpleName();

    public static Intent newIntent(Context context) {
        return new Intent(context, MultiViewTypePagerActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
