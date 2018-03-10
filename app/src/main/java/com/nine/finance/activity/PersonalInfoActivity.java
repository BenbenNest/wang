package com.nine.finance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.business.UserManager;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.UserInfo;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.RegexUtils;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CircleAvatarView;
import com.nine.finance.view.CommonButton;
import com.nine.finance.view.CommonHeadView;
import com.nine.finance.view.PersonalInfoRow;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PersonalInfoActivity extends BaseActivity {

    CircleAvatarView avatarView;
    PersonalInfoRow idInfoView;
    PersonalInfoRow nameInfoView;
    PersonalInfoRow nickNameInfoView;
    PersonalInfoRow phoneInfoView;
    PersonalInfoRow telInfoView;
    PersonalInfoRow addressInfoView;

    CommonButton btSubmit;
    static final int request_code = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        init();
    }

    private void init() {
        avatarView = (CircleAvatarView) findViewById(R.id.info_avatar);
        avatarView.setImageResource(R.drawable.head_default);
        idInfoView = (PersonalInfoRow) findViewById(R.id.info_id);
        nameInfoView = (PersonalInfoRow) findViewById(R.id.info_name);
        nickNameInfoView = (PersonalInfoRow) findViewById(R.id.info_nick_name);
        phoneInfoView = (PersonalInfoRow) findViewById(R.id.info_mobile);
        telInfoView = (PersonalInfoRow) findViewById(R.id.info_tel);
        addressInfoView = (PersonalInfoRow) findViewById(R.id.info_address);
        btSubmit = (CommonButton) findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });
        fillUserinfo(AppGlobal.getUserInfo());
        ((CommonHeadView) findViewById(R.id.head_view)).setAction("退出", new CommonHeadView.OnActionListener() {
            @Override
            public void onAction() {
                logOut();
            }
        });
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AvatarActivity.startActivityForResult(PersonalInfoActivity.this, request_code);
            }
        });
    }

    private void fillUserinfo(UserInfo userInfo) {
        if (userInfo != null) {
            idInfoView.setText(AppGlobal.getUserInfo().getIDNum());
            nameInfoView.setText(AppGlobal.getUserInfo().getName());
            nickNameInfoView.setText(AppGlobal.getUserInfo().getNickName());
            phoneInfoView.setText(AppGlobal.getUserInfo().getMobile());
            telInfoView.setText(AppGlobal.getUserInfo().getTel());
            addressInfoView.setText(AppGlobal.getUserInfo().getAddress());
            Glide.with(PersonalInfoActivity.this).load(AppGlobal.getUserInfo().getHead()).into(avatarView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == requestCode) {
                if (data != null) {
                    String path = data.getStringExtra("path");
                    Glide.with(PersonalInfoActivity.this).load(path).into(avatarView);
                }
            }
        }
    }

    private void updateUserInfo() {
        if (!NetUtil.isNetworkConnectionActive(PersonalInfoActivity.this)) {
            ToastUtils.showCenter(PersonalInfoActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        if (AppGlobal.getUserInfo() == null) {
            ToastUtils.showCenter(this, "信息修改失败！");
            return;
        }
        if (!RegexUtils.isMobile(phoneInfoView.getText().toString().trim())) {
            ToastUtils.showCenter(this, "请填写正确的手机号！");
            return;
        }
        Map<String, String> para = new HashMap<>();

//        {
//            "id": "5D3C80C6090146EF90C18C61DB2C5136",
//
//                "username": "string",
//                "password": "string",
//                "phone": "string",
//                "name": "string",
//                "nationality": "string",
//                "nativePlace": "string",
//                "card": "string",
//                "gender": "string",
//                "ethnic": "string",
//                "birthday": "1987-08-01",
//                "address": "string"
//        }

        para.put("id", AppGlobal.getUserInfo().getUserId());
        para.put("username", AppGlobal.getUserInfo().getName());
//        para.put("nationality", AppGlobal.getUserInfo().getNationality());
//        para.put("nativePlace", AppGlobal.getUserInfo().getInativePlaced());
//        para.put("gender", AppGlobal.getUserInfo().getGender());
//        para.put("ethnic", AppGlobal.getUserInfo().getEthnic());
//        para.put("birthday", AppGlobal.getUserInfo().getBirthday());
        para.put("address", addressInfoView.getText().toString().trim());
        para.put("phone", phoneInfoView.getText().toString().trim());
        para.put("tel", AppGlobal.getUserInfo().getTel());
//        para.put("card", AppGlobal.getUserInfo().getIDNum());

        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<UserInfo>> call = api.updateUserinfo(body);
        call.enqueue(new Callback<BaseModel<UserInfo>>() {
            @Override
            public void onResponse(Call<BaseModel<UserInfo>> call, Response<BaseModel<UserInfo>> response) {
                try {
                    if (response != null || response.body() != null) {
                        if (BaseModel.SUCCESS.equals(response.body().status)) {
                            UserInfo loginData = response.body().content;
                            fillUserinfo(loginData);
                            AppGlobal.setUserInfo(loginData);
                            UserManager.saveUserData(getApplicationContext(), loginData);
//                        String token = response.body().data.access_token;
//                        getUserMessage(response.body().data, token, uiCallback);
//                        SharedPreferenceUtils.getInstance(FellowAppEnv.getAppContext()).saveMessage("token", token);
                        }
                    }
                } catch (Exception e) {
                    Log.d("", e.getMessage());
                    ToastUtils.showCenter(PersonalInfoActivity.this, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<UserInfo>> call, Throwable t) {
                Log.d("", t.getMessage());
                ToastUtils.showCenter(PersonalInfoActivity.this, t.getMessage());
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
