package com.nine.finance.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.VerifyCodeModel;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.RegexUtils;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonInputLayout;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FillMobileActivity extends BaseActivity {
    CommonInputLayout mPhoneInputLayout;
    CommonInputLayout mVerifyCodeInputLayout;
    String phone;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_mobile);
        init();
    }

    private void init() {
        mPhoneInputLayout = (CommonInputLayout) findViewById(R.id.phone_input_layout);
        mVerifyCodeInputLayout = (CommonInputLayout) findViewById(R.id.verify_code_input_layout);
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegexUtils.isMobile(mPhoneInputLayout.getText())) {
                    code = mVerifyCodeInputLayout.getText();
                    if (TextUtils.isEmpty(code)) {
                        ToastUtils.showCenter(FillMobileActivity.this, "请填写验证码");
                    } else {
                        verifyCode();
                    }
                } else {
                    ToastUtils.showCenter(FillMobileActivity.this, "请填写正确的手机号");
                }
            }
        });
        findViewById(R.id.bt_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegexUtils.isMobile(mPhoneInputLayout.getText())) {
                    phone = mPhoneInputLayout.getText();
                    getVerifyCode();
                } else {
                    ToastUtils.showCenter(FillMobileActivity.this, "请填写正确的手机号");
                }
            }
        });
    }

    private void getVerifyCode() {
        if (!NetUtil.isNetworkConnectionActive(FillMobileActivity.this)) {
            ToastUtils.showCenter(FillMobileActivity.this, getResources().getString(R.string.net_not_connect));
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
                if (response != null && response.code() == 200 && response.body() != null) {
                    VerifyCodeModel data = response.body().content;
                } else {
                    ToastUtils.showCenter(FillMobileActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<VerifyCodeModel>> call, Throwable t) {
                ToastUtils.showCenter(FillMobileActivity.this, t.getMessage());
            }
        });
    }

    private void verifyCode() {
        if (!NetUtil.isNetworkConnectionActive(FillMobileActivity.this)) {
            ToastUtils.showCenter(FillMobileActivity.this, getResources().getString(R.string.net_not_connect));
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
                if (response != null && response.code() == 200 && response.message() == BaseModel.SUCCESS) {
                    startActivity(FillMobileActivity.this, BindBankCardActivity.class);
                } else {
                    ToastUtils.showCenter(FillMobileActivity.this, response.message());
                    startActivity(FillMobileActivity.this, BindBankCardActivity.class);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                ToastUtils.showCenter(FillMobileActivity.this, t.getMessage());
                startActivity(FillMobileActivity.this, BindBankCardActivity.class);
            }
        });
    }


}
