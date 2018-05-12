package com.nine.finance.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.permission.PermissionDialogUtils;
import com.nine.finance.permission.PermissionUtils;
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

import static com.nine.finance.permission.Permissions.REQUEST_CODE_CAMERA;

public class VerifyCodeActivity extends BaseActivity {
    static final int request_code = 100;
    CommonInputLayout mCodeInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        commonHeadView = (CommonHeadView) findViewById(R.id.head_view);
        if (commonHeadView != null) {
            commonHeadView.setStep(R.drawable.step8);
        }
        mCodeInputLayout = (CommonInputLayout) findViewById(R.id.code_input_layout);
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                OpenglActivity.startActivity(VerifyCodeActivity.this);
//                startActivity(VerifyCodeActivity.this, SubmitApplyActivity.class);
//                FaceActivity.startActivity(VerifyCodeActivity.this);
                if (!PermissionUtils.checkSDPermission(VerifyCodeActivity.this)) {
                    PermissionUtils.requestSDAndCameraPermission(VerifyCodeActivity.this);
                } else {
//                    FaceActivity.startActivity(VerifyCodeActivity.this);
                    if (TextUtils.isEmpty(mCodeInputLayout.getText())) {
                        ToastUtils.showCenter(VerifyCodeActivity.this, "请输入验证码");
                    } else {
                        verifyCode();
//                        FacePicActivity.startActivity(VerifyCodeActivity.this);
                    }
                }
            }
        });
    }

    private void verifyCode() {
        final String phone = getIntent().getStringExtra("phone");
        final String code = mCodeInputLayout.getText();
        if (!NetUtil.isNetworkConnectionActive(VerifyCodeActivity.this)) {
            ToastUtils.showCenter(VerifyCodeActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();
        para.put("phone", phone);
        para.put("code", mCodeInputLayout.getText());
        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        Gson gson = new Gson();
        String strEntity = gson.toJson(para);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

        Call<BaseModel<String>> call = api.verifyCode(phone, code);
        call.enqueue(new Callback<BaseModel<String>>() {
            @Override
            public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                if (response != null && response.code() == 200 && response.body() != null && response.body().status != null && response.body().status.equals(BaseModel.SUCCESS)) {
                    FacePicActivity.startActivity(VerifyCodeActivity.this);
                } else {
                    ToastUtils.showCenter(VerifyCodeActivity.this, response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                ToastUtils.showCenter(VerifyCodeActivity.this, t.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE_CAMERA == requestCode) {
            if (!PermissionUtils.checkSDPermission(this) && !PermissionUtils.checkCameraPermission(this)) {
                PermissionDialogUtils.showSDAndCameraPermissionDialog(this);
            } else if (!PermissionUtils.checkSDPermission(this)) {
                PermissionDialogUtils.showSDPermissionDialog(this);
            } else if (!PermissionUtils.checkCameraPermission(this)) {
                PermissionDialogUtils.showCameraPermissionDialog(this);
            } else {
//                FaceActivity.startActivity(VerifyCodeActivity.this);
                FacePicActivity.startActivity(VerifyCodeActivity.this);
            }
        }
    }


}
