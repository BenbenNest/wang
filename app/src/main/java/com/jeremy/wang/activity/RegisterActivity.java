package com.jeremy.wang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jeremy.wang.R;
import com.jeremy.wang.thread.NoLeakHandler;
import com.jeremy.wang.utils.Constant;
import com.jeremy.wang.utils.PreferenceUtils;
import com.jeremy.wang.utils.RegexUtils;
import com.jeremy.wang.utils.ToastUtils;
import com.jeremy.wang.view.CommonInputLayout;

public class RegisterActivity extends Activity {
    CommonInputLayout mIdInputLayout;
    CommonInputLayout mPasswordInputLayout;
    CommonInputLayout mPasswordAgainInputLayout;
    CommonInputLayout mPhoneInputLayout;
    CommonInputLayout mVerifyCodeInputLayout;

    String id, pwd, pwdAgain, phone, verifyCode;
    NoLeakHandler noLeakHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        noLeakHandler = new NoLeakHandler(this);
        init();
    }

    private void init() {
        mIdInputLayout = (CommonInputLayout) findViewById(R.id.id_input_layout);
        mPasswordInputLayout = (CommonInputLayout) findViewById(R.id.password_input_layout);
        mPasswordAgainInputLayout = (CommonInputLayout) findViewById(R.id.password_again_input_layout);
        mPhoneInputLayout = (CommonInputLayout) findViewById(R.id.phone_input_layout);
        mVerifyCodeInputLayout = (CommonInputLayout) findViewById(R.id.verify_code_input_layout);
        TextView tv_Contract = (TextView) findViewById(R.id.tv_contract);
        String contract = getResources().getString(R.string.contract);
        SpannableStringBuilder style = new SpannableStringBuilder(getResources().getString(R.string.contract));
        int start = contract.lastIndexOf("《");
        int end = contract.length();
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_contract)), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_Contract.setText(style);
        initRegisterButton();

    }

    private void initRegisterButton() {
        findViewById(R.id.bt_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = mIdInputLayout.getText();
                pwd = mPasswordInputLayout.getText();
                pwdAgain = mPasswordAgainInputLayout.getText();
                phone = mPhoneInputLayout.getText();
                verifyCode = mVerifyCodeInputLayout.getText();
                if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(verifyCode)) {
                    ToastUtils.showCenter(RegisterActivity.this, "信息填写不完整");
                    return;
                }
                if (!pwd.equals(pwdAgain)) {
                    ToastUtils.showCenter(RegisterActivity.this, "密码输入不一致");
                    return;
                }
                if (!RegexUtils.isIDCard(id)) {
                    ToastUtils.showCenter(RegisterActivity.this, "身份证号码错误");
                    return;
                }
                if (!RegexUtils.isMobile(phone)) {
                    ToastUtils.showCenter(RegisterActivity.this, "手机号码不正确");
                    return;
                }
                CheckBox chkContract = (CheckBox) findViewById(R.id.chk_contract);
                if (!chkContract.isChecked()) {
                    ToastUtils.showCenter(RegisterActivity.this, "请同意用户协议");
                    return;
                }
                saveAccountInfo();
                ToastUtils.showCenter(RegisterActivity.this, "注册成功！");
                noLeakHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }, 1000);

            }
        });
    }

    private void saveAccountInfo() {
        String id = mIdInputLayout.getText();
        String pwd = mPasswordInputLayout.getText();
        PreferenceUtils.saveData(this, Constant.PREFERENCE_ACCOUNT_ID, id);
        PreferenceUtils.saveData(this, Constant.PREFERENCE_ACCOUNT_PWD, pwd);
    }

    public void login(View view) {
        LoginActivity.startActivity(RegisterActivity.this);
    }


}
