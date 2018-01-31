package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jeremy.wang.R;
import com.jeremy.wang.business.UserManager;
import com.jeremy.wang.view.BusinessRectView;


public class HomeActivity extends BaseActivity {

    BusinessRectView financial_view;

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
        setTitle("首页");
        financial_view = (BusinessRectView) findViewById(R.id.financial_view);
        financial_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.checkLogin(HomeActivity.this)) {
                    BankCardListActivity.startActivity(HomeActivity.this);
                } else {
                    LoginActivity.startActivity(HomeActivity.this);
                }
            }
        });
        findViewById(R.id.iv_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HomeActivity.this, PersonalInfoActivity.class);
            }
        });
    }
}
