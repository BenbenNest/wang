package com.nine.finance.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonInputLayout;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SubmitApplyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_apply);


        findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonInputLayout mPasswordInputLayout = (CommonInputLayout) findViewById(R.id.password_input_layout);
                CommonInputLayout mPasswordAgainInputLayout = (CommonInputLayout) findViewById(R.id.password_again_input_layout);

                String pwd = mPasswordInputLayout.getText();
                String pwdAgain = mPasswordAgainInputLayout.getText();

                if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)) {
                    ToastUtils.showCenter(SubmitApplyActivity.this, "请输入密码");
                    return;
                }
                if (!pwd.equals(pwdAgain)) {
                    ToastUtils.showCenter(SubmitApplyActivity.this, "密码输入不一致");
                    return;
                }

                ToastUtils.showCenter(SubmitApplyActivity.this, "申请成功！");

                noLeakHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(SubmitApplyActivity.this, HomeActivity.class);
                    }
                }, 2000);
            }
        });
    }

    private void apply() {
        if (!NetUtil.isNetworkConnectionActive(SubmitApplyActivity.this)) {
            ToastUtils.showCenter(SubmitApplyActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();

//
//        {
//            "id": "5D3C80C6090146EF90C18C61DB2C5137",
//                "createDate": "2016-08-01 12:24:36",
//                "updateDate": "2016-08-01 12:24:36",
//                "bankId": "string",
//                "cardNumber": "string",
//                "phone": "string",
//                "userId": "string",
//                "isAccountAgreement": "string",
//                "isPlatformAgreement": "string",
//                "name": "string",
//                "nationality": "string",
//                "nativePlace": "string",
//                "card": "string",
//                "gender": "string",
//                "ethnic": "string",
//                "birthday": "1987-08-01",
//                "address": "string",
//                "deliveryAddress": "string",
//                "status": "string",
//                "logisticsCompany": "string",
//                "shipmentNumber": "string"
//        }


        para.put("id", "");
        para.put("createDate", "");
        para.put("updateDate", "");
        para.put("bankId", "");

        para.put("cardNumber", "");
        para.put("phone", "");
        para.put("userId", "");
        para.put("isAccountAgreement", "");
        para.put("isPlatformAgreement", "");
        para.put("name", "");
        para.put("nationality", "");
        para.put("nativePlace", "");
        para.put("card", "");
        para.put("gender", "");
        para.put("ethnic", "");
        para.put("birthday", "");
        para.put("address", "");
        para.put("deliveryAddress", "");
        para.put("status", "");
        para.put("logisticsCompany", "");
        para.put("shipmentNumber", "");


        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);


        Call<BaseModel<String>> call = api.applyCard(body);
        call.enqueue(new Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                try {
                    if (response != null || response.body() != null) {
                        if (BaseModel.SUCCESS.equals(response.body().status)) {
                            ToastUtils.showCenter(SubmitApplyActivity.this, "申请成功！");
                            noLeakHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(SubmitApplyActivity.this, HomeActivity.class);
                                }
                            }, 2000);
                        }
                    }
                } catch (Exception e) {
                    Log.d("", e.getMessage());
                    ToastUtils.showCenter(SubmitApplyActivity.this, e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                Log.d("", t.getMessage());
                ToastUtils.showCenter(SubmitApplyActivity.this, t.getMessage());
            }
        });
    }


}
