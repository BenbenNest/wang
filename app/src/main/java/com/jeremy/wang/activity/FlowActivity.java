package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jeremy.wang.R;

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
