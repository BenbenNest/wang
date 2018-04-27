package com.nine.finance.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.api.OnBooleanListener;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.constant.Constant;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.idcard.AuthManager;
import com.nine.finance.idcard.IDCardScanActivity;
import com.nine.finance.idcard.util.Screen;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.UserInfo;
import com.nine.finance.model.VerifyCodeModel;
import com.nine.finance.thread.NoLeakHandler;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.PreferenceUtils;
import com.nine.finance.utils.RegexUtils;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonInputLayout;
import com.nine.finance.view.TimeCountDown;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends BaseActivity implements TimeCountDown.OnTimerCountDownListener, AuthManager.AuthCallBack, CommonInputLayout.OnScanListener {
    CommonInputLayout mIdInputLayout;
    CommonInputLayout mNameInputLayout;
    CommonInputLayout mAgeInputLayout;
    CommonInputLayout mPasswordInputLayout;
    CommonInputLayout mPasswordAgainInputLayout;
    CommonInputLayout mPhoneInputLayout;
    CommonInputLayout mVerifyCodeInputLayout;
    CommonInputLayout mContactInputLayout;
    CommonInputLayout mAddressInputLayout;
    TimeCountDown mCountDownButton;
    boolean canGetCode = true;
    String id, name, birthday, pwd, pwdAgain, phone, verifyCode, address, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        noLeakHandler = new NoLeakHandler(this);
        Screen.initialize(this);
        init();
    }

    private void init() {
        mIdInputLayout = (CommonInputLayout) findViewById(R.id.id_input_layout);
        mIdInputLayout.setOnScanListener(this);
        mNameInputLayout = (CommonInputLayout) findViewById(R.id.name_input_layout);
        mAgeInputLayout = (CommonInputLayout) findViewById(R.id.name_input_layout);
        mPasswordInputLayout = (CommonInputLayout) findViewById(R.id.password_input_layout);
        mPasswordAgainInputLayout = (CommonInputLayout) findViewById(R.id.password_again_input_layout);
        mPhoneInputLayout = (CommonInputLayout) findViewById(R.id.phone_input_layout);
        mVerifyCodeInputLayout = (CommonInputLayout) findViewById(R.id.verify_code_input_layout);
        mContactInputLayout = (CommonInputLayout) findViewById(R.id.contact_input_layout);
        mAddressInputLayout = (CommonInputLayout) findViewById(R.id.address_input_layout);

        TextView tv_Contract = (TextView) findViewById(R.id.tv_contract);
        String contract = getResources().getString(R.string.contract);
        SpannableStringBuilder style = new SpannableStringBuilder(getResources().getString(R.string.contract));
        int start = contract.lastIndexOf("《");
        int end = contract.length();
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_contract)), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_Contract.setText(style);
        initRegisterButton();
        mCountDownButton = (TimeCountDown) findViewById(R.id.bt_verify);
        mCountDownButton.setOnTimerCountDownListener(this);
        findViewById(R.id.bt_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (RegexUtils.isMobile(mPhoneInputLayout.getText())) {
                        phone = mPhoneInputLayout.getText();
                        if (canGetCode) {
                            getVerifyCode();
                            mCountDownButton.initTimer();
                        } else {
                            ToastUtils.showCenter(RegisterActivity.this, "请稍后再发！");
                        }
                    } else {
                        ToastUtils.showCenter(RegisterActivity.this, "请填写正确的手机号");
                    }
                } catch (Exception e) {
                    Log.e("jeremy", e.getMessage());
                }
            }
        });
        mIdInputLayout.setOnTextChangeListener(watcher);
    }

    boolean verified = false;
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString().trim();
            if (verified) {
                if (text.length() < 17) {
                    if (verified)
                        verified = false;
                }
            } else {
                if (text.length() == 17) {
                    String lastCode = RegexUtils.getLastOfIDCard(text);
                    if (TextUtils.isEmpty(lastCode)) {

                    } else {
                        s.append(lastCode);
                        mIdInputLayout.setText(s.toString());
                        verified = true;
                    }
                }
            }
        }
    };

    private void getVerifyCode() {
        if (!NetUtil.isNetworkConnectionActive(RegisterActivity.this)) {
            ToastUtils.showCenter(RegisterActivity.this, getResources().getString(R.string.net_not_connect));
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
//                    VerifyCodeModel data = response.body().content;
                } else {
                    ToastUtils.showCenter(RegisterActivity.this, response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<VerifyCodeModel>> call, Throwable t) {
                ToastUtils.showCenter(RegisterActivity.this, t.getMessage());
            }
        });
    }

    private void initRegisterButton() {
        findViewById(R.id.bt_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = mIdInputLayout.getText();
                name = mNameInputLayout.getText();
                birthday = mAgeInputLayout.getText();
                pwd = mPasswordInputLayout.getText();
                pwdAgain = mPasswordAgainInputLayout.getText();
                phone = mPhoneInputLayout.getText();
                verifyCode = mVerifyCodeInputLayout.getText();
                address = mAddressInputLayout.getText();
                if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(verifyCode)) {
                    ToastUtils.showCenter(RegisterActivity.this, "信息填写不完整");
                    return;
                }
                if (!pwd.equals(pwdAgain)) {
                    ToastUtils.showCenter(RegisterActivity.this, "密码输入不一致");
                    return;
                }
                if (id.length() != 18) {
                    ToastUtils.showCenter(RegisterActivity.this, "身份证位数不正确");
                    return;
                }
                if (!RegexUtils.isIDCard(id)) {
                    ToastUtils.showCenter(RegisterActivity.this, "身份证号码错误");
                    return;
                }
                if (TextUtils.isEmpty(mAgeInputLayout.getText())) {
                    ToastUtils.showCenter(RegisterActivity.this, "请填写生日");
                    return;
                }
                if (!RegexUtils.isMobile(phone)) {
                    ToastUtils.showCenter(RegisterActivity.this, "手机号码不正确");
                    return;
                }
                CheckBox chkContract = (CheckBox) findViewById(R.id.chk_contract);
                if (!chkContract.isChecked()) {
                    ToastUtils.showCenter(RegisterActivity.this, "请同意用户协议");
                    return;
                }
                saveAccountInfo();
                register();
                noLeakHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }, 1000);

            }
        });
    }

    private void register() {
        if (!NetUtil.isNetworkConnectionActive(RegisterActivity.this)) {
            ToastUtils.showCenter(RegisterActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();

//        "IDNum",
//                "name",
//                "nickName",
//                "mobile",
//                "tel",
//                "address",
//                "head",
//      “password"

        para.put("nickName", "");
        para.put("mobile", phone);
        para.put("tel", "");
        para.put("address", address);
        para.put("name", name);
        para.put("birthday", birthday);
        para.put("password", pwd);
        para.put("card", id);
        para.put("username", id);
        para.put("code", verifyCode);

//        para.put("nickName", "");
//        para.put("mobile", "13581665443");
//        para.put("tel", "");
//        para.put("address", "asdfasf");
//        para.put("name", "dasfds");
//        para.put("password", "123456");
//        para.put("card", "230404198309060519");
//        para.put("username", "asfd");
//        para.put("code", "234");
        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);


        Call<BaseModel<UserInfo>> call = api.register(body);
        call.enqueue(new Callback<BaseModel<UserInfo>>() {
            @Override
            public void onResponse(Call<BaseModel<UserInfo>> call, Response<BaseModel<UserInfo>> response) {
                try {
                    if (response != null || response.body() != null && response.body().status != null) {
                        if (BaseModel.SUCCESS.equals(response.body().status)) {
                            startActivity(RegisterActivity.this, LoginActivity.class);
//                        String token = response.body().data.access_token;
//                        getUserMessage(response.body().data, token, uiCallback);
//                        SharedPreferenceUtils.getInstance(FellowAppEnv.getAppContext()).saveMessage("token", token);
                        }
                    }
                } catch (Exception e) {
                    Log.d("", e.getMessage());
                    ToastUtils.showCenter(RegisterActivity.this, e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<BaseModel<UserInfo>> call, Throwable t) {
                Log.d("", t.getMessage());
                ToastUtils.showCenter(RegisterActivity.this, t.getMessage());
            }
        });
    }

    private void saveAccountInfo() {
        String id = mIdInputLayout.getText();
        String pwd = mPasswordInputLayout.getText();
        PreferenceUtils.saveData(this, Constant.PREFERENCE_ACCOUNT_ID, id);
        PreferenceUtils.saveData(this, Constant.PREFERENCE_ACCOUNT_PWD, pwd);
    }

    public void login(View view) {
        LoginActivity.startActivity(RegisterActivity.this);
    }


    @Override
    public void onCountDownStart() {
        canGetCode = false;
    }

    @Override
    public void onCountDownLoading(int currentCount) {

    }

    @Override
    public void onCountDownError() {

    }

    @Override
    public void onCountDownFinish() {
        canGetCode = true;
    }


    private static final int REQUEST_IDCARDSCAN_CODE = 100;

    @Override
    public void onScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  // N以上的申请权限实例
            Log.d("MainActivity", "进入权限");
            onPermissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
                @Override
                public void onClick(boolean bln) {
                    if (bln) {
                        AuthManager.checkIDCardAuthState(RegisterActivity.this, RegisterActivity.this);
                    } else {
                        Toast.makeText(RegisterActivity.this, "扫码拍照或无法正常使用", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(RegisterActivity.this,
                                new String[]{Manifest.permission.CAMERA}, 10);
                    }
                }
            });
        } else {
            authState(true);
        }
    }

    @Override
    public void authState(boolean flag) {
        if (true) {
//            OpenglActivity.startActivity(IDCardActivity.this);
//            startActivity(IDCardActivity.this, OpenglActivity.class);
            Intent intent = new Intent(this, IDCardScanActivity.class);
            intent.putExtra("isvertical", true);
            intent.putExtra("isClearShadow", false);
            intent.putExtra("isTextDetect", false);
            intent.putExtra("isDebug", false);
            intent.putExtra("bound", 0.8);
            intent.putExtra("idcard", 0.1);
            intent.putExtra("clear", 0.8);
            startActivityForResult(intent, REQUEST_IDCARDSCAN_CODE);
        } else {
            ToastUtils.showCenter(RegisterActivity.this, "权限授权失败");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_IDCARDSCAN_CODE) {
            String path = data.getStringExtra("path");
            String IDCardInfo = data.getStringExtra("info");
            try {
                JSONObject jObject = new JSONObject(IDCardInfo).getJSONArray("cards").getJSONObject(0);
                if ("back".equals(jObject.getString("side"))) {
                    ToastUtils.showCenter(RegisterActivity.this, "请扫描身份证正面");
                } else {
                    String address = jObject.getString("address");
                    birthday = jObject.getString("birthday");
                    mAgeInputLayout.setText(birthday);
                    AppGlobal.getApplyModel().setBirthday(birthday);
                    String gender = jObject.getString("gender");
                    AppGlobal.getApplyModel().setGender(gender);
                    String id_card_number = jObject.getString("id_card_number");
                    AppGlobal.getApplyModel().setIdCard(id_card_number);
                    String name = jObject.getString("name");
                    AppGlobal.getApplyModel().setName(name);
                    String race = jObject.getString("race");
                    String side = jObject.getString("side");
                    mIdInputLayout.setText(id_card_number);
                    mNameInputLayout.setText(name);
                }
            } catch (Exception e) {

            }

        }
    }


}
