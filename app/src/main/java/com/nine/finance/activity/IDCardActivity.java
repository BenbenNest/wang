package com.nine.finance.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nine.finance.R;
import com.nine.finance.api.OnBooleanListener;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.idcard.AuthManager;
import com.nine.finance.idcard.IDCardScanActivity;
import com.nine.finance.idcard.util.Screen;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.ImageInfo;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonHeadView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IDCardActivity extends BaseActivity implements AuthManager.AuthCallBack {

    private ImageView mIvDirect;
    private ImageView mIvNonDirect;
    public static boolean mIsDirect = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        Screen.initialize(this);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_IDCARDSCAN_CODE) {
            String path = data.getStringExtra("path");
            String IDCardInfo = data.getStringExtra("info");
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                if (mIsDirect) {
//                    AppGlobal.getApplyModel().setIdCardImageFront(path);
                    mIvDirect.setImageBitmap(bitmap);
                } else {
//                    AppGlobal.getApplyModel().setIdCardImageBack(path);
                    mIvNonDirect.setImageBitmap(bitmap);
                }
                uploadFile(path);
            }
        }
    }

    public void uploadFile(String path) {
        if (!NetUtil.isNetworkConnectionActive(IDCardActivity.this)) {
            ToastUtils.showCenter(IDCardActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Retrofit retrofit = new RetrofitService().getRetrofit();
        APIInterface api = retrofit.create(APIInterface.class);

        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multiPart = MultipartBody.Part.createFormData("file", path, requestBody);
        Call<BaseModel<ImageInfo>> call = api.uploadFile(multiPart);
        call.enqueue(new Callback<BaseModel<ImageInfo>>() {
            @Override
            public void onResponse(Call<BaseModel<ImageInfo>> call, Response<BaseModel<ImageInfo>> response) {
                if (response != null && response.code() == 200 && response.body() != null) {
                    ImageInfo imageInfo = response.body().content;
                    if (AppGlobal.getApplyModel() != null) {
                        if (mIsDirect) {
                            AppGlobal.getApplyModel().setIdCardImageFront(imageInfo);
                        } else {
                            AppGlobal.getApplyModel().setIdCardImageBack(imageInfo);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<ImageInfo>> call, Throwable t) {

            }
        });
    }

    private void init() {
        commonHeadView = (CommonHeadView) findViewById(R.id.head_view);
        if (commonHeadView != null) {
            commonHeadView.setStep(R.drawable.step3);
        }
        mIvNonDirect = (ImageView) findViewById(R.id.id_direct_no);
        mIvDirect = (ImageView) findViewById(R.id.id_direct);
        mIvDirect.setOnClickListener(new IDCardOnclickListener(true));
        mIvNonDirect.setOnClickListener(new IDCardOnclickListener(false));
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppGlobal.mIDCardFront == null || AppGlobal.mIDCardBack == null) {
                    ToastUtils.showCenter(IDCardActivity.this, "请完成身份证扫描");
                } else if (AppGlobal.getApplyModel().getIdCardImageFront() == null) {
                    ToastUtils.showCenter(IDCardActivity.this, "身份证正面上传失败");
//                    startActivity(IDCardActivity.this, FillAccountInfoActivity.class);
                } else if (AppGlobal.getApplyModel().getIdCardImageBack() == null) {
                    ToastUtils.showCenter(IDCardActivity.this, "身份证反面上传失败");
                } else if (!AppGlobal.getApplyModel().getIdCard().equals(AppGlobal.getUserInfo().getIDNum())) {
                    ToastUtils.showCenter(IDCardActivity.this, "该身份证和注册时使用的身份证信息不一致!");
                } else {
                    startActivity(IDCardActivity.this, FillAccountInfoActivity.class);
                }
            }
        });
    }

    class IDCardOnclickListener implements View.OnClickListener {
        public IDCardOnclickListener(boolean direct) {
//            mIsDirect = direct;
        }

        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  // N以上的申请权限实例
                Log.d("MainActivity", "进入权限");
                onPermissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
                    @Override
                    public void onClick(boolean bln) {
                        if (bln) {
                            AuthManager.checkIDCardAuthState(IDCardActivity.this, IDCardActivity.this);
                        } else {
                            Toast.makeText(IDCardActivity.this, "扫码拍照或无法正常使用", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(IDCardActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, 10);
                        }
                    }
                });
            } else {
                AuthManager.checkIDCardAuthState(IDCardActivity.this, IDCardActivity.this);
            }
        }
    }

    private static final int REQUEST_IDCARDSCAN_CODE = 100;

    @Override
    public void authState(boolean flag) {
        if (flag) {
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
            ToastUtils.showCenter(IDCardActivity.this, "权限授权失败");
        }
    }
}
