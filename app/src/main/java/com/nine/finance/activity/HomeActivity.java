package com.nine.finance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nine.finance.R;
import com.nine.finance.business.UserManager;
import com.nine.finance.view.BusinessRectView;


public class HomeActivity extends BaseActivity {

    BusinessRectView accountRectView;

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
        initListener();
        accountRectView = (BusinessRectView) findViewById(R.id.create_account);
        accountRectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.checkLogin(HomeActivity.this)) {
                    MyApplyBankListActivity.startActivity(HomeActivity.this);
                } else {
                    LoginActivity.startActivity(HomeActivity.this);
//                    MyApplyBankListActivity.startActivity(HomeActivity.this);
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

    private void initListener() {
        findViewById(R.id.iv_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.checkLogin(HomeActivity.this)) {
                    MyApplyBankListActivity.startActivity(HomeActivity.this);
                } else {
                    //TODO test
                    LoginActivity.startActivity(HomeActivity.this);
//                    MyApplyBankListActivity.startActivity(HomeActivity.this);
                }
            }
        });
//        findViewById(R.id.iv_goods).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (UserManager.checkLogin(HomeActivity.this)) {
//                    MyApplyBankListActivity.startActivity(HomeActivity.this);
//                } else {
//                    LoginActivity.startActivity(HomeActivity.this);
//                }
//            }
//        });
//        findViewById(R.id.iv_account).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (UserManager.checkLogin(HomeActivity.this)) {
//                    MyApplyBankListActivity.startActivity(HomeActivity.this);
//                } else {
//                    LoginActivity.startActivity(HomeActivity.this);
//                }
//            }
//        });
//        findViewById(R.id.iv_account).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (UserManager.checkLogin(HomeActivity.this)) {
//                    MyApplyBankListActivity.startActivity(HomeActivity.this);
//                } else {
//                    LoginActivity.startActivity(HomeActivity.this);
//                }
//            }
//        });
    }
}