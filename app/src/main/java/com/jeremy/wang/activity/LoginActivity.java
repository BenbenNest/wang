package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jeremy.wang.R;
import com.jeremy.wang.utils.Constant;
import com.jeremy.wang.utils.PreferenceUtils;
import com.jeremy.wang.utils.ToastUtils;
import com.jeremy.wang.view.CommonInputLayout;

public class LoginActivity extends AppCompatActivity {
    TextView bt_register;
    TextView bt_login;
    CommonInputLayout idInputLayout;
    CommonInputLayout pwdInputLayout;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    private void initView() {
        bt_register = (TextView) findViewById(R.id.bt_register);
        bt_login = (TextView) findViewById(R.id.bt_login);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        idInputLayout = (CommonInputLayout) findViewById(R.id.id_input_layout);
        pwdInputLayout = (CommonInputLayout) findViewById(R.id.password_input_layout);

        String id = PreferenceUtils.getData(LoginActivity.this, Constant.PREFERENCE_ACCOUNT_ID, "").toString();
        String pwd = PreferenceUtils.getData(LoginActivity.this, Constant.PREFERENCE_ACCOUNT_PWD, "").toString();
        idInputLayout.setText(id);
        pwdInputLayout.setText(pwd);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = idInputLayout.getText();
                String pwd = pwdInputLayout.getText();
                if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pwd)) {
                    ToastUtils.showCenter(LoginActivity.this, "请检查登录信息");
                    return;
                }
                if (!id.equals(id) || !pwd.equals(pwd)) {
                    ToastUtils.showCenter(LoginActivity.this, "身份证号码或者密码错误");
                    return;
                }
                HomeActivity.startActivity(LoginActivity.this);

//                boolean logined = false;
//                if (logined) {
//                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(LoginActivity.this, FaceScanActivity.class);
//                    startActivity(intent);
//                }
            }
        });
    }


}
