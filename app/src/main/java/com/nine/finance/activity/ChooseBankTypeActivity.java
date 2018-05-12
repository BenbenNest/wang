package com.nine.finance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.adapter.SpinnerAdapter;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.VerifyCodeModel;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonHeadView;
import com.nine.finance.view.CommonInputLayout;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChooseBankTypeActivity extends BaseActivity {
    CommonInputLayout mPhoneInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bank_type);
        init();
    }

    private void init() {
        commonHeadView = (CommonHeadView) findViewById(R.id.head_view);
        if (commonHeadView != null) {
            commonHeadView.setStep(R.drawable.step7);
        }
        mPhoneInputLayout = (CommonInputLayout) findViewById(R.id.contact_input_layout);
        Spinner spinnerType = (Spinner) findViewById(R.id.spinner_banktype);
        String[] types = {"储蓄卡"};
        ArrayAdapter<String> adapter;
        adapter = new SpinnerAdapter(this, types);
        //设置下拉列表的风格,simple_spinner_dropdown_item是android系统自带的样式，等会自定义修改
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinnerType.setAdapter(adapter);

        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerifyCode();
//                verify();
            }
        });
    }

    private void getVerifyCode() {
        if (!NetUtil.isNetworkConnectionActive(ChooseBankTypeActivity.this)) {
            ToastUtils.showCenter(ChooseBankTypeActivity.this, getResources().getString(R.string.net_not_connect));
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
                    verify();
//                    VerifyCodeModel data = response.body().content;
                } else {
                    ToastUtils.showCenter(ChooseBankTypeActivity.this, response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<VerifyCodeModel>> call, Throwable t) {
                ToastUtils.showCenter(ChooseBankTypeActivity.this, t.getMessage());
            }
        });
    }

    private void verify() {
        Intent intent = new Intent(ChooseBankTypeActivity.this, VerifyCodeActivity.class);
        intent.putExtra("phone", mPhoneInputLayout.getText());
        startActivity(intent);
//        startActivity(ChooseBankTypeActivity.this, VerifyCodeActivity.class);
        if (true) return;
        if (!NetUtil.isNetworkConnectionActive(ChooseBankTypeActivity.this)) {
            ToastUtils.showCenter(ChooseBankTypeActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();
        if (AppGlobal.getApplyModel() != null) {
            para.put("cardNo", AppGlobal.getApplyModel().getCardNumber());//6227000011120044185
            para.put("customerNm", AppGlobal.getApplyModel().getName());//王龙鹤
            para.put("phoneNo", AppGlobal.getApplyModel().getPhone());//15001334852
            para.put("certifId", AppGlobal.getApplyModel().getIdCard());//230404198309060519
//            para.put("cardNo", "6227000011120044185");//6227000011120044185
//            para.put("customerNm", "test");//王龙鹤
//            para.put("phoneNo", "15001334852");//15001334852
//            para.put("certifId", "230404198309060519");//230404198309060519
        }

        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<VerifyCodeModel>> call = api.elementsRecognition(body);

        call.enqueue(new Callback<BaseModel<VerifyCodeModel>>() {
            @Override
            public void onResponse(Call<BaseModel<VerifyCodeModel>> call, Response<BaseModel<VerifyCodeModel>> response) {
                Log.d("jeremy", response.message() + "-----" + response.body().message);
                if (response != null && response.code() == 200 && response.body() != null && response.body().status.equals(BaseModel.SUCCESS)) {
                    VerifyCodeModel model = response.body().content;
                    startActivity(ChooseBankTypeActivity.this, VerifyCodeActivity.class);
                } else {
//                    ToastUtils.showCenter(ChooseBankTypeActivity.this, "填写信息没有通过银行验证，请重新检查");
                    startActivity(ChooseBankTypeActivity.this, VerifyCodeActivity.class);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<VerifyCodeModel>> call, Throwable t) {
                Log.d("jeremy", t.getMessage());
                ToastUtils.showCenter(ChooseBankTypeActivity.this, "请求失败，请重试！");
//                startActivity(ChooseBankTypeActivity.this, VerifyCodeActivity.class);
            }
        });
    }

}
