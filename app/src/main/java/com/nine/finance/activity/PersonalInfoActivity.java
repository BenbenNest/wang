package com.nine.finance.activity;

import android.os.Bundle;
import android.view.View;

import com.nine.finance.R;
import com.nine.finance.business.UserManager;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PersonalInfoActivity extends BaseActivity {

    CommonButton btLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        init();
    }

    private void init() {
        btLogout = (CommonButton) findViewById(R.id.bt_logout);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    public void logOut() {
        if (!NetUtil.isNetworkConnectionActive(PersonalInfoActivity.this)) {
            ToastUtils.showCenter(PersonalInfoActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);
        Call<BaseModel<Boolean>> call = api.logOut();
        call.enqueue(new Callback<BaseModel<Boolean>>() {
            @Override
            public void onResponse(Call<BaseModel<Boolean>> call, Response<BaseModel<Boolean>> response) {
                if (response != null || response.body() != null && response.body().content) {
                    UserManager.logOut(PersonalInfoActivity.this);
                    startActivity(PersonalInfoActivity.this, HomeActivity.class);
                } else {
                    ToastUtils.showCenter(PersonalInfoActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Boolean>> call, Throwable t) {
                ToastUtils.showCenter(PersonalInfoActivity.this, t.getMessage());
            }
        });
    }


}
