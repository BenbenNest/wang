package com.nine.finance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.constant.Constant;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BankIntroContract;
import com.nine.finance.model.BaseModel;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonHeadView;
import com.nine.finance.view.MyWebView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BankContractActivity extends BaseActivity {
    private CommonHeadView headView;
    private MyWebView webView;

    public static void startActivity(Context context, String bankId) {
        Intent intent = new Intent(context, BankContractActivity.class);
        intent.putExtra("bankId", bankId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_contract);
        init();
    }

    private void init() {
        commonHeadView = (CommonHeadView) findViewById(R.id.head_view);
        if (commonHeadView != null) {
            commonHeadView.setStep(R.drawable.step2);
        }
        webView = (MyWebView) findViewById(R.id.webview);
        getContent(Constant.BANK_CONTRACT);

        String url = getIntent().getStringExtra("url");
        String bankId = getIntent().getStringExtra("bankId");
        getContent(bankId);

        final CheckBox chkAgree = (CheckBox) findViewById(R.id.chk_bank_contract);
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkAgree.isChecked()) {
                    startActivity(BankContractActivity.this, IDCardActivity.class);
                } else {
                    ToastUtils.showCenter(BankContractActivity.this, "请同意开户协议");
                }
            }
        });
    }

    private void getContent(String bankId) {
        if (!NetUtil.isNetworkConnectionActive(BankContractActivity.this)) {
            ToastUtils.showCenter(BankContractActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();

        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<BankIntroContract>> call = api.getBankIntroOrContract(bankId);
        call.enqueue(new Callback<BaseModel<BankIntroContract>>() {
            @Override
            public void onResponse(Call<BaseModel<BankIntroContract>> call, Response<BaseModel<BankIntroContract>> response) {
                if (response != null && response.code() == 200 && response.body() != null && response.body().content != null) {
                    BankIntroContract introContract = response.body().content;
                    if (webView != null) {
//                        webView.loadData(htmlData, "text/html", "UTF -8");//API提供的标准用法，无法解决乱码问题
                        webView.loadData(introContract.getAgreementStr(), "text/html; charset=UTF-8", null);//这种写法可以正确解码
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<BankIntroContract>> call, Throwable t) {
                webView.loadData(t.getMessage(), "text/html; charset=UTF-8", null);
            }
        });
    }

}

