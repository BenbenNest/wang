package com.nine.finance.activity;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BankInfo;
import com.nine.finance.model.BaseModel;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonInputLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FillMobileActivity extends BaseActivity {
    CommonInputLayout mPhoneInputLayout;
    CommonInputLayout mVerifyCodeInputLayout;

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
                verifyCode();
            }
        });
        findViewById(R.id.bt_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });
    }

    private void requestData() {
        if (!NetUtil.isNetworkConnectionActive(FillMobileActivity.this)) {
            ToastUtils.showCenter(FillMobileActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();
        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<List<BankInfo>>> call = api.getApplyBankList(body);
        call.enqueue(new Callback<BaseModel<List<BankInfo>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<BankInfo>>> call, Response<BaseModel<List<BankInfo>>> response) {
                if (response != null && response.code() == 200) {

                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<BankInfo>>> call, Throwable t) {
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
        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<List<BankInfo>>> call = api.getApplyBankList(body);
        call.enqueue(new Callback<BaseModel<List<BankInfo>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<BankInfo>>> call, Response<BaseModel<List<BankInfo>>> response) {
                if (response != null && response.code() == 200) {
                    startActivity(FillMobileActivity.this, BindBankCardActivity.class);
                } else {
                    ToastUtils.showCenter(FillMobileActivity.this, response.message());
                    startActivity(FillMobileActivity.this, BindBankCardActivity.class);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<BankInfo>>> call, Throwable t) {
                ToastUtils.showCenter(FillMobileActivity.this, t.getMessage());
                startActivity(FillMobileActivity.this, BindBankCardActivity.class);
            }
        });
    }


}
