package com.jeremy.wang.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.jeremy.wang.R;
import com.jeremy.wang.constant.Constant;
import com.jeremy.wang.utils.PreferenceUtils;
import com.jeremy.wang.utils.ToastUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkLoginInfo();
    }

    private void checkLoginInfo() {
        if (TextUtils.isEmpty(PreferenceUtils.getData(SplashActivity.this, Constant.PREFERENCE_ACCOUNT_ID, "").toString()) ||
                TextUtils.isEmpty(PreferenceUtils.getData(SplashActivity.this, Constant.PREFERENCE_ACCOUNT_PWD, "").toString())) {
            ToastUtils.showCenter(SplashActivity.this, "身份证号码或者密码错误");
            LoginActivity.startActivity(SplashActivity.this);
        } else {
            HomeActivity.startActivity(SplashActivity.this);
        }
    }

}
