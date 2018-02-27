package com.nine.finance.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.adapter.SpinnerAdapter;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChooseBankTypeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bank_type);
        init();
    }

    private void init() {
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
                verify();
            }
        });
    }

    private void verify() {
        if (!NetUtil.isNetworkConnectionActive(ChooseBankTypeActivity.this)) {
            ToastUtils.showCenter(ChooseBankTypeActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();

        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<String>> call = api.elementsRecognition(body);
        call.enqueue(new Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                if (response != null && response.code() == 200 && response.body() != null && response.body().status.equals(BaseModel.SUCCESS)) {
                    String model = response.body().content;
                    startActivity(ChooseBankTypeActivity.this, VerifyCodeActivity.class);
                } else {
                    ToastUtils.showCenter(ChooseBankTypeActivity.this, "填写信息没有通过银行验证，请重新检查");
                }
            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                ToastUtils.showCenter(ChooseBankTypeActivity.this,"请求失败，请重试！");
            }
        });
    }

}
