package com.nine.finance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.app.AppPreference;
import com.nine.finance.business.UserManager;
import com.nine.finance.constant.Constant;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.UserInfo;
import com.nine.finance.password.ForgetPasswordActivity;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.PreferenceUtils;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonButton;
import com.nine.finance.view.CommonInputLayout;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
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
        idInputLayout.setText("230404198309060519");
        pwdInputLayout.setText("1");
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
        findViewById(R.id.bt_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.this, ForgetPasswordActivity.class);
            }
        });
    }

    private void login() {
        if (!NetUtil.isNetworkConnectionActive(LoginActivity.this)) {
            ToastUtils.showCenter(LoginActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();
//        para.put("app_id", Constant.APPID);
//        para.put("country_code", Constant.COUNTRY_CODE);
//        para.put("password", StringUtil.shaEncrypt(pwdInputLayout.getText()));
//        para.put("mobile", phone.toString());
        para.put("name", idInputLayout.getText());
        para.put("password", pwdInputLayout.getText().toString().trim());
//        para=Urlb
        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<UserInfo>> call = api.login(body);
        call.enqueue(new Callback<BaseModel<UserInfo>>() {
            @Override
            public void onResponse(Call<BaseModel<UserInfo>> call, Response<BaseModel<UserInfo>> response) {
                try {
                    if (response == null || response.body() == null || response.body().content == null) {
                        ToastUtils.showCenter(LoginActivity.this, "登录失败，请稍后重试");
                    } else if (response.code() == 200) {
                        if (BaseModel.SUCCESS.equals(response.body().status)) {
                            UserInfo loginData = response.body().content;
                            loginData.setIDNum(idInputLayout.getText().toString().trim());
                            AppGlobal.setUserInfo(loginData);
                            rememberUser();
                            UserManager.saveUserData(getApplicationContext(), loginData);
                            HomeActivity.startActivity(LoginActivity.this);
                        } else {
                            ToastUtils.showCenter(LoginActivity.this, response.body().message);
                        }
                    }//http://39.106.173.14:8088/account/rest/account/user/login
                } catch (Exception e) {
                    Log.d("", e.getMessage());
                }
//                HomeActivity.startActivity(LoginActivity.this);
            }

            @Override
            public void onFailure(Call<BaseModel<UserInfo>> call, Throwable t) {
                ToastUtils.showCenter(LoginActivity.this, t.getMessage());
//                HomeActivity.startActivity(LoginActivity.this);
            }
        });


    }


    private void rememberUser() {
        CheckBox chkRemember = (CheckBox) findViewById(R.id.chk_remember);
        if (chkRemember.isChecked()) {
            AppPreference.setRememberUserName(LoginActivity.this, true);
        } else {
            AppPreference.setRememberUserName(LoginActivity.this, false);
        }
    }


}
