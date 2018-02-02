package com.jeremy.wang.activity;

import android.os.Bundle;
import android.view.View;

import com.jeremy.wang.R;

public class IDCardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        init();
    }


    private void init() {
        findViewById(R.id.id_direct).setOnClickListener(onClickListener);
        findViewById(R.id.id_direct_no).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(IDCardActivity.this, IDCardUploadActivity.class);
        }
    };

}
