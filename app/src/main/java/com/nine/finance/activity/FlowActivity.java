package com.nine.finance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nine.finance.R;

public class FlowActivity extends BaseActivity {
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FlowActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
    }


}
