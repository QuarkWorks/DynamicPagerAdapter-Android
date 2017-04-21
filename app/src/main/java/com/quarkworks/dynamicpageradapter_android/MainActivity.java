package com.quarkworks.dynamicpageradapter_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.quarkworks.dynamicpageradapter_android.multi_view_type.MultiPagerActivity;
import com.quarkworks.dynamicpageradapter_android.single_view_type.SinglePagerActivity;

/**
 * @author jacobamuchow@gmail.com
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        View singleButton = findViewById(R.id.main_activity_single_button);
        View multiButton = findViewById(R.id.main_activity_multi_button);

        singleButton.setOnClickListener(singleClickListener);
        multiButton.setOnClickListener(multiClickListener);
    }

    /**
     * Listeners
     */
    private final View.OnClickListener singleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = SinglePagerActivity.newIntent(MainActivity.this);
            startActivity(intent);
        }
    };

    private final View.OnClickListener multiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = MultiPagerActivity.newIntent(MainActivity.this);
            startActivity(intent);
        }
    };
}
