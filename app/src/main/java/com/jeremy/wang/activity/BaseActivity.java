package com.jeremy.wang.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeremy.wang.thread.NoLeakHandler;

public class BaseActivity extends AppCompatActivity {
    NoLeakHandler noLeakHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noLeakHandler = new NoLeakHandler(this);
    }


    protected void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }


}
