package com.nine.finance.activity;

import android.os.Bundle;
import android.view.View;

import com.nine.finance.R;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.view.CommonInputLayout;

public class BindBankCardActivity extends BaseActivity {
    CommonInputLayout nameInputLayout;
    CommonInputLayout bankcardInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank_card);
        init();
    }


    private void init() {
        nameInputLayout = (CommonInputLayout) findViewById(R.id.name_input_layout);
        bankcardInputLayout = (CommonInputLayout) findViewById(R.id.bankcard_input_layout);
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppGlobal.getApplyModel().mBankCardNum = bankcardInputLayout.getText().toString().trim();
                startActivity(BindBankCardActivity.this, ChooseBankTypeActivity.class);
            }
        });
    }

}
