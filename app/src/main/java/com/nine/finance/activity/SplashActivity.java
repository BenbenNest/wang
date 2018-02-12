package com.nine.finance.activity;

import android.os.Bundle;

import com.nine.finance.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkLoginInfo();
    }

    private void checkLoginInfo() {
        startActivity(this, LoginActivity.class);
//        if (TextUtils.isEmpty(PreferenceUtils.getData(SplashActivity.this, Constant.PREFERENCE_ACCOUNT_ID, "").toString()) ||
//                TextUtils.isEmpty(PreferenceUtils.getData(SplashActivity.this, Constant.PREFERENCE_ACCOUNT_PWD, "").toString())) {
//            ToastUtils.showCenter(SplashActivity.this, "身份证号码或者密码错误");
//            LoginActivity.startActivity(SplashActivity.this);
//        } else {
//            HomeActivity.startActivity(SplashActivity.this);
//        }
    }

}
