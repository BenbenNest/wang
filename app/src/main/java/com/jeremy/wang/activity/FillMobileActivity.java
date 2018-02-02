package com.jeremy.wang.activity;

import android.os.Bundle;
import android.view.View;

import com.jeremy.wang.R;

public class FillMobileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_mobile);
        init();
    }

    private void init() {
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FillMobileActivity.this, BindBankCardActivity.class);
            }
        });
    }

}
