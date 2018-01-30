package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jeremy.wang.R;

public class ChooseBankActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChooseBankActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bank);
        init();
    }

    private void init() {
        findViewById(R.id.bt_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceScanActivity.startActivity(ChooseBankActivity.this);
            }
        });
    }

}
