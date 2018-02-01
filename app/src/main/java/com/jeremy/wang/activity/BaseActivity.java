package com.jeremy.wang.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jeremy.wang.R;
import com.jeremy.wang.thread.NoLeakHandler;
import com.jeremy.wang.view.CommonHeadView;

public class BaseActivity extends Activity implements CommonHeadView.OnBackListener {
    CommonHeadView commonHeadView;

    public static void startActivity(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    NoLeakHandler noLeakHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noLeakHandler = new NoLeakHandler(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        commonHeadView = (CommonHeadView) findViewById(R.id.head_view);
        if (commonHeadView != null) {
            commonHeadView.setOnBackListener(this);
        }
    }

    protected void setTitle(String title) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }


    @Override
    public void onBack() {
        super.onBackPressed();
    }
}
