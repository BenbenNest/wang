package com.nine.finance.activity;

import android.os.Bundle;
import android.view.View;

import com.nine.finance.R;

public class FillAccountInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_account_info);
        init();
    }

    private void init() {
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FillAccountInfoActivity.this, FillMobileActivity.class);
            }
        });
    }

}
