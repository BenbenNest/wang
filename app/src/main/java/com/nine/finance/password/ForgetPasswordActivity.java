package com.nine.finance.password;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.activity.BaseActivity;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.VerifyCodeModel;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.RegexUtils;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonHeadView;
import com.nine.finance.view.CommonInputLayout;
import com.nine.finance.view.TimeCountDown;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForgetPasswordActivity extends BaseActivity implements TimeCountDown.OnTimerCountDownListener {
    CommonInputLayout mPhoneInputLayout;
    CommonInputLayout mVerifyCodeInputLayout;
    TimeCountDown mCountDownButton;
    String phone;
    String code;
    boolean canGetCode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        init();
    }

    private void init() {
        commonHeadView = (CommonHeadView) findViewById(R.id.head_view);
//        if (commonHeadView != null) {
//            commonHeadView.setStep(R.drawable.step5);
//        }
        mPhoneInputLayout = (CommonInputLayout) findViewById(R.id.phone_input_layout);
        mVerifyCodeInputLayout = (CommonInputLayout) findViewById(R.id.verify_code_input_layout);
        mCountDownButton = (TimeCountDown) findViewById(R.id.bt_verify);
        mCountDownButton.setOnTimerCountDownListener(this);
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegexUtils.isMobile(mPhoneInputLayout.getText())) {
                    code = mVerifyCodeInputLayout.getText();
                    if (TextUtils.isEmpty(code)) {
                        ToastUtils.showCenter(ForgetPasswordActivity.this, "请填写验证码");
                    } else {
                        verifyCode();
                    }
                } else {
                    ToastUtils.showCenter(ForgetPasswordActivity.this, "请填写正确的手机号");
                }
            }
        });
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
                            ToastUtils.showCenter(ForgetPasswordActivity.this, "请稍后再发！");
                        }
                    } else {
                        ToastUtils.showCenter(ForgetPasswordActivity.this, "请填写正确的手机号");
                    }
                } catch (Exception e) {
                    Log.e("jeremy", e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownButton != null) {
            mCountDownButton.cancel();
        }
    }

    private void getVerifyCode() {
        if (!NetUtil.isNetworkConnectionActive(ForgetPasswordActivity.this)) {
            ToastUtils.showCenter(ForgetPasswordActivity.this, getResources().getString(R.string.net_not_connect));
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
                    ToastUtils.showCenter(ForgetPasswordActivity.this, response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<VerifyCodeModel>> call, Throwable t) {
                ToastUtils.showCenter(ForgetPasswordActivity.this, t.getMessage());
            }
        });
    }

    private void verifyCode() {
        if (!NetUtil.isNetworkConnectionActive(ForgetPasswordActivity.this)) {
            ToastUtils.showCenter(ForgetPasswordActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();
        para.put("phone", phone);
        para.put("code", code);
        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<String>> call = api.verifyCode(phone, code);
        call.enqueue(new Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                if (response != null && response.code() == 200 && response.body() != null && response.body().status != null && response.body().status.equals(BaseModel.SUCCESS)) {
                    Intent intent = new Intent(ForgetPasswordActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("code", code);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
//                    startActivity(ForgetPasswordActivity.this, ResetPasswordActivity.class);
                } else {
                    ToastUtils.showCenter(ForgetPasswordActivity.this, response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                ToastUtils.showCenter(ForgetPasswordActivity.this, t.getMessage());
            }
        });
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
