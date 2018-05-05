package com.nine.finance.password;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.activity.BaseActivity;
import com.nine.finance.activity.LoginActivity;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
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

public class ResetPasswordActivity extends BaseActivity {
    CommonInputLayout passwordInputLayout;
    CommonInputLayout passwordAgainInputLayout;
    String password, passwordAgain, code, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
    }

    private void init() {
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
        commonHeadView = (CommonHeadView) findViewById(R.id.head_view);
        passwordInputLayout = (CommonInputLayout) findViewById(R.id.password_input_layout);
        passwordAgainInputLayout = (CommonInputLayout) findViewById(R.id.password_again_input_layout);
        findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = passwordInputLayout.getText();
                passwordAgain = passwordAgainInputLayout.getText();
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showCenter(ResetPasswordActivity.this, " 密码不能为空");
                } else if (!password.equals(passwordAgain)) {
                    ToastUtils.showCenter(ResetPasswordActivity.this, "两次密码输入不一致");
                } else {
                    updatePassword();
                }
            }
        });
    }

    private void updatePassword() {
        if (!NetUtil.isNetworkConnectionActive(ResetPasswordActivity.this)) {
            ToastUtils.showCenter(ResetPasswordActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();
        para.put("phone", phone);
        para.put("code", code);
        para.put("password", password);
        para.put("type", "4");
        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

//        Call<BaseModel<String>> call = api.forgetPassword(body);
        Call<BaseModel<String>> call = api.forgetPassword(phone, code, "4", password);
        call.enqueue(new Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                if (response != null && response.code() == 200 && response.body() != null && response.body().status != null && response.body().status.equals(BaseModel.SUCCESS)) {
                    startActivity(ResetPasswordActivity.this, LoginActivity.class);
                    ResetPasswordActivity.this.finish();
                } else {
                    ToastUtils.showCenter(ResetPasswordActivity.this, response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                ToastUtils.showCenter(ResetPasswordActivity.this, t.getMessage());
            }
        });
    }


}
