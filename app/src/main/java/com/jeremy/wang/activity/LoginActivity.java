package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jeremy.wang.R;
import com.jeremy.wang.app.AppGlobal;
import com.jeremy.wang.constant.Constant;
import com.jeremy.wang.http.APIInterface;
import com.jeremy.wang.http.RetrofitService;
import com.jeremy.wang.model.BaseModel;
import com.jeremy.wang.model.UserLoginData;
import com.jeremy.wang.utils.NetUtil;
import com.jeremy.wang.utils.PreferenceUtils;
import com.jeremy.wang.utils.StringUtil;
import com.jeremy.wang.utils.ToastUtils;
import com.jeremy.wang.view.CommonButton;
import com.jeremy.wang.view.CommonInputLayout;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity {
    TextView bt_register;
    CommonButton bt_login;
    CommonInputLayout idInputLayout;
    CommonInputLayout pwdInputLayout;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    private void initView() {
        bt_register = (TextView) findViewById(R.id.bt_register);
        bt_login = (CommonButton) findViewById(R.id.bt_login);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        idInputLayout = (CommonInputLayout) findViewById(R.id.id_input_layout);
        pwdInputLayout = (CommonInputLayout) findViewById(R.id.password_input_layout);
        String id = PreferenceUtils.getData(LoginActivity.this, Constant.PREFERENCE_ACCOUNT_ID, "").toString();
        String pwd = PreferenceUtils.getData(LoginActivity.this, Constant.PREFERENCE_ACCOUNT_PWD, "").toString();
        idInputLayout.setText(id);
        pwdInputLayout.setText(pwd);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idInputLayout.getText();
                String pwd = pwdInputLayout.getText();
                if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pwd)) {
                    ToastUtils.showCenter(LoginActivity.this, "请检查登录信息");
                    return;
                }
                if (!id.equals(id) || !pwd.equals(pwd)) {
                    ToastUtils.showCenter(LoginActivity.this, "身份证号码或者密码错误");
                    return;
                }
//                HomeActivity.startActivity(LoginActivity.this);
                login();

            }
        });
    }


    private void login() {
        if (!NetUtil.isNetworkConnectionActive(LoginActivity.this)) {
            ToastUtils.showCenter(LoginActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();
        para.put("app_id", Constant.APPID);
        para.put("country_code", Constant.COUNTRY_CODE);
        para.put("password", StringUtil.shaEncrypt(pwdInputLayout.getText()));
//        para.put("mobile", phone.toString());
        para.put("name", idInputLayout.getText());
        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);
        Call<BaseModel<UserLoginData>> call = api.login(para);
        call.enqueue(new Callback<BaseModel<UserLoginData>>() {
            @Override
            public void onResponse(Call<BaseModel<UserLoginData>> call, Response<BaseModel<UserLoginData>> response) {
                try {
                    if (response == null || response.body() == null || response.body().data == null) {
                        ToastUtils.showCenter(LoginActivity.this, "登录失败，请稍后重试");
                    } else if (response.code() == 200) {
                        if (BaseModel.SUCCESS.equals(response.body().result_code)) {
                            String token = response.body().data.access_token;
                            AppGlobal.mUserLoginData = response.body().data;
                            HomeActivity.startActivity(LoginActivity.this);
//                        getUserMessage(response.body().data, token, uiCallback);
//                        SharedPreferenceUtils.getInstance(FellowAppEnv.getAppContext()).saveMessage("token", token);
                        } else {
                            ToastUtils.showCenter(LoginActivity.this, response.body().msg);
                        }
                    }
                } catch (Exception e) {
                    Log.d("", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<BaseModel<UserLoginData>> call, Throwable t) {

            }
        });


    }


}
