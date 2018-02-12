package com.nine.finance.activity;

import android.os.Bundle;
import android.view.View;

import com.nine.finance.R;

public class VerifyCodeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(VerifyCodeActivity.this, DynamicRecognizeActivity.class);
            }
        });
    }


}
