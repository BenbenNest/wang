package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jeremy.wang.R;


public class HomeActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("首页");
        findViewById(R.id.financial_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankCardListActivity.startActivity(HomeActivity.this);
            }
        });
    }
}
