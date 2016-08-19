package com.quarkworks.dynamicviewpager_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.quarkworks.dynamicviewpager_demo.multi_view_type.PagerActivity;

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
            Intent intent = com.quarkworks.dynamicviewpager_demo.single_view_type.PagerActivity.newIntent(MainActivity.this);
            startActivity(intent);
        }
    };

    private final View.OnClickListener multiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = PagerActivity.newIntent(MainActivity.this);
            startActivity(intent);
        }
    };
}
