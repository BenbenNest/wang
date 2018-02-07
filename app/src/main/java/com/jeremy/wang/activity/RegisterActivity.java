package com.jeremy.wang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jeremy.wang.R;
import com.jeremy.wang.constant.Constant;
import com.jeremy.wang.http.APIInterface;
import com.jeremy.wang.http.RetrofitService;
import com.jeremy.wang.model.BaseModel;
import com.jeremy.wang.model.UserLoginData;
import com.jeremy.wang.thread.NoLeakHandler;
import com.jeremy.wang.utils.PreferenceUtils;
import com.jeremy.wang.utils.RegexUtils;
import com.jeremy.wang.utils.StringUtil;
import com.jeremy.wang.utils.ToastUtils;
import com.jeremy.wang.view.CommonInputLayout;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends BaseActivity {
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
                register();
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

    private void register() {
        Map<String, String> para = new HashMap<>();
        para.put("app_id", Constant.APPID);
        para.put("country_code", Constant.COUNTRY_CODE);

        para.put("password", StringUtil.shaEncrypt(mPasswordInputLayout.getText()));
        para.put("id", phone.toString());
        para.put("headSculpture", "");
        para.put("phoneNumber", "");
        para.put("address", "");
        para.put("idNumber", "");


        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);
        Call<BaseModel<UserLoginData>> call = api.register(para);
        call.enqueue(new Callback<BaseModel<UserLoginData>>() {
            @Override
            public void onResponse(Call<BaseModel<UserLoginData>> call, Response<BaseModel<UserLoginData>> response) {
                try {
                    if (response == null || response.body() == null || response.body().data == null) {

                    } else if (BaseModel.SUCCESS.equals(response.body().result_code)) {
                        String token = response.body().data.access_token;
//                        getUserMessage(response.body().data, token, uiCallback);
//                        SharedPreferenceUtils.getInstance(FellowAppEnv.getAppContext()).saveMessage("token", token);
                    }
                } catch (Exception e) {
                    Log.d("", e.getMessage());
                }
                startActivity(RegisterActivity.this, LoginActivity.class);
            }

            @Override
            public void onFailure(Call<BaseModel<UserLoginData>> call, Throwable t) {

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
