package com.nine.finance.activity;

import android.os.Bundle;
import android.view.View;

import com.nine.finance.R;

public class FaceRecognizeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognize);
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FaceRecognizeActivity.this, SubmitApplyActivity.class);
            }
        });
    }
}
