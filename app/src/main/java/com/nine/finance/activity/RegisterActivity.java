package com.nine.finance.activity;

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

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.constant.Constant;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.UserInfo;
import com.nine.finance.model.VerifyCodeModel;
import com.nine.finance.thread.NoLeakHandler;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.PreferenceUtils;
import com.nine.finance.utils.RegexUtils;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonInputLayout;
import com.nine.finance.view.TimeCountDown;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends BaseActivity implements TimeCountDown.OnTimerCountDownListener {
    CommonInputLayout mIdInputLayout;
    CommonInputLayout mPasswordInputLayout;
    CommonInputLayout mPasswordAgainInputLayout;
    CommonInputLayout mPhoneInputLayout;
    CommonInputLayout mVerifyCodeInputLayout;
    CommonInputLayout mContactInputLayout;
    CommonInputLayout mAddressInputLayout;
    TimeCountDown mCountDownButton;
    boolean canGetCode = true;
    String id, pwd, pwdAgain, phone, verifyCode, address, code;
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
        mContactInputLayout = (CommonInputLayout) findViewById(R.id.contact_input_layout);
        mAddressInputLayout = (CommonInputLayout) findViewById(R.id.address_input_layout);

        TextView tv_Contract = (TextView) findViewById(R.id.tv_contract);
        String contract = getResources().getString(R.string.contract);
        SpannableStringBuilder style = new SpannableStringBuilder(getResources().getString(R.string.contract));
        int start = contract.lastIndexOf("《");
        int end = contract.length();
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_contract)), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_Contract.setText(style);
        initRegisterButton();
        mCountDownButton = (TimeCountDown) findViewById(R.id.bt_verify);
        mCountDownButton.setOnTimerCountDownListener(this);
        findViewById(R.id.bt_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (RegexUtils.isMobile(mPhoneInputLayout.getText())) {
                        phone = mPhoneInputLayout.getText();
                        if (canGetCode) {
                            getVerifyCode();
                            mCountDownButton.initTimer();
                        } else {
                            ToastUtils.showCenter(RegisterActivity.this, "请稍后再发！");
                        }
                    } else {
                        ToastUtils.showCenter(RegisterActivity.this, "请填写正确的手机号");
                    }
                } catch (Exception e) {
                    Log.e("jeremy", e.getMessage());
                }
            }
        });
    }

    private void getVerifyCode() {
        if (!NetUtil.isNetworkConnectionActive(RegisterActivity.this)) {
            ToastUtils.showCenter(RegisterActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();
        String phone = mPhoneInputLayout.getText();
        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<VerifyCodeModel>> call = api.getVerifyCode(phone);
        call.enqueue(new Callback<BaseModel<VerifyCodeModel>>() {
            @Override
            public void onResponse(Call<BaseModel<VerifyCodeModel>> call, Response<BaseModel<VerifyCodeModel>> response) {
                if (response != null && response.code() == 200 && response.body() != null && response.body().status.equals(BaseModel.SUCCESS)) {
                    VerifyCodeModel data = response.body().content;
                } else {
                    ToastUtils.showCenter(RegisterActivity.this, response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<VerifyCodeModel>> call, Throwable t) {
                ToastUtils.showCenter(RegisterActivity.this, t.getMessage());
            }
        });
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
                address = mAddressInputLayout.getText();
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
        if (!NetUtil.isNetworkConnectionActive(RegisterActivity.this)) {
            ToastUtils.showCenter(RegisterActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();

//        "IDNum",
//                "name",
//                "nickName",
//                "mobile",
//                "tel",
//                "address",
//                "head",
//      “password"

        para.put("nickName", "");
        para.put("mobile", phone);
        para.put("tel", "");
        para.put("address", address);
        para.put("name", id);
        para.put("password", pwd);
        para.put("card", id);
        para.put("username", id);
        para.put("code", verifyCode);
        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);


        Call<BaseModel<UserInfo>> call = api.register(body);
        call.enqueue(new Callback<BaseModel<UserInfo>>() {
            @Override
            public void onResponse(Call<BaseModel<UserInfo>> call, Response<BaseModel<UserInfo>> response) {
                try {
                    if (response != null || response.body() != null) {
                        if (BaseModel.SUCCESS.equals(response.body().status)) {
                            startActivity(RegisterActivity.this, LoginActivity.class);
//                        String token = response.body().data.access_token;
//                        getUserMessage(response.body().data, token, uiCallback);
//                        SharedPreferenceUtils.getInstance(FellowAppEnv.getAppContext()).saveMessage("token", token);
                        }
                    }
                } catch (Exception e) {
                    Log.d("", e.getMessage());
                    ToastUtils.showCenter(RegisterActivity.this, e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<BaseModel<UserInfo>> call, Throwable t) {
                Log.d("", t.getMessage());
                ToastUtils.showCenter(RegisterActivity.this, t.getMessage());
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


    @Override
    public void onCountDownStart() {
        canGetCode = false;
    }

    @Override
    public void onCountDownLoading(int currentCount) {

    }

    @Override
    public void onCountDownError() {

    }

    @Override
    public void onCountDownFinish() {
        canGetCode = true;
    }
}
