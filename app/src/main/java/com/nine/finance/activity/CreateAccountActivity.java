package com.nine.finance.activity;

import android.os.Bundle;
import android.view.View;

import com.nine.finance.R;
import com.nine.finance.view.CommonButton;

public class CreateAccountActivity extends BaseActivity {

    CommonButton btAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        init();
    }

    private void init() {
        setTitle("填写开户信息");
        btAction = (CommonButton) findViewById(R.id.bt_action);
        btAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CreateAccountActivity.this, FaceScanActivity.class);
            }
        });
    }

}
