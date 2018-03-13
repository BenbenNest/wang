package com.nine.finance.activity.bank;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nine.finance.R;
import com.nine.finance.activity.BaseActivity;
import com.nine.finance.activity.FaceActivity;
import com.nine.finance.activity.SubmitApplyActivity;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.camera.CameraHelper;
import com.nine.finance.camera.MaskSurfaceView;
import com.nine.finance.camera.OnCaptureCallback;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.ImageInfo;
import com.nine.finance.permission.PermissionDialogUtils;
import com.nine.finance.permission.PermissionUtils;
import com.nine.finance.utils.DisplayUtils;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonHeadView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.nine.finance.permission.Permissions.REQUEST_CODE_CAMERA;

public class BankCardScanActivity extends BaseActivity implements OnCaptureCallback {

    private MaskSurfaceView surfaceview;
    private ImageView imageView;
    //	拍照
    private Button btn_capture;
    //	重拍
    private Button btn_recapture;
    //	取消
    private Button btn_cancel;
    //	确认
    private Button btn_ok;

    //	拍照后得到的保存的文件路径
    private String filepath;
    private int faceWidth = 300;
    private int faceHeight = 200;
//    private float IDCARD_RATIO = 856.0f / 540.0f;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FaceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_bank_card_scan2);
        commonHeadView = (CommonHeadView) findViewById(R.id.head_view);
        if (commonHeadView != null) {
            commonHeadView.setStep(R.drawable.step9);
        }
        this.surfaceview = (MaskSurfaceView) findViewById(R.id.surface_view);
        this.imageView = (ImageView) findViewById(R.id.image_view);
        btn_capture = (Button) findViewById(R.id.btn_capture);
        btn_recapture = (Button) findViewById(R.id.btn_recapture);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

//		设置矩形区域大小
        int width = DisplayUtils.dp2px(BankCardScanActivity.this, faceWidth);
        int height = DisplayUtils.dp2px(BankCardScanActivity.this, faceHeight);
        this.surfaceview.setMaskSize(width, height);

//		拍照
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

//		重拍
        btn_recapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_capture.setEnabled(true);
                btn_ok.setEnabled(false);
                btn_recapture.setEnabled(false);
                imageView.setVisibility(View.GONE);
                surfaceview.setVisibility(View.VISIBLE);
                deleteFile();
                CameraHelper.getInstance().startPreview();
            }
        });

//		确认
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!TextUtils.isEmpty(filepath)) {
                    uploadFile(filepath);
                }
            }
        });

//		取消
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                deleteFile();
                BankCardScanActivity.this.finish();
            }
        });

        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(filepath)) {
                    ToastUtils.showCenter(BankCardScanActivity.this, "请拍照");
                } else {
                    apply();
//                    startActivity(FaceActivity.this, SubmitApplyActivity.class);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                takePhoto();
            }
        }
    }

    private void takePhoto() {
        btn_capture.setEnabled(false);
        btn_ok.setEnabled(true);
        btn_recapture.setEnabled(true);
        CameraHelper.getInstance().tackPicture(BankCardScanActivity.this);
    }

    /**
     * 删除图片文件呢
     */
    private void deleteFile() {
        if (this.filepath == null || this.filepath.equals("")) {
            return;
        }
        File f = new File(this.filepath);
        if (f.exists()) {
            f.delete();
        }
    }

    public void uploadFile(String path) {
        if (!NetUtil.isNetworkConnectionActive(BankCardScanActivity.this)) {
            ToastUtils.showCenter(BankCardScanActivity.this, getResources().getString(R.string.net_not_connect));
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
                    AppGlobal.getApplyModel().setFaceImage(imageInfo);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<ImageInfo>> call, Throwable t) {
                ToastUtils.showCenter(BankCardScanActivity.this, t.getMessage());
            }
        });
    }

    @Override
    public void onCapture(boolean success, String filepath) {
        this.filepath = filepath;
        String message = "拍照成功";
        if (!success) {
            message = "拍照失败";
            CameraHelper.getInstance().startPreview();
            this.imageView.setVisibility(View.GONE);
            this.surfaceview.setVisibility(View.VISIBLE);
        } else {
            this.imageView.setVisibility(View.VISIBLE);
            this.surfaceview.setVisibility(View.GONE);
            this.imageView.setImageBitmap(BitmapFactory.decodeFile(filepath));
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void apply() {
        if (!NetUtil.isNetworkConnectionActive(BankCardScanActivity.this)) {
            ToastUtils.showCenter(BankCardScanActivity.this, getResources().getString(R.string.net_not_connect));
            return;
        }
        Map<String, String> para = new HashMap<>();

        if (AppGlobal.getApplyModel() != null) {
//            para.put("id", "");
//        para.put("createDate", "");
//        para.put("updateDate", "");
            para.put("bankId", AppGlobal.getApplyModel().getBankId());

            para.put("cardNumber", AppGlobal.getApplyModel().getCardNumber());
            para.put("phone", AppGlobal.getApplyModel().getPhone());
            para.put("userId", AppGlobal.getUserInfo().getUserId());
//        para.put("isAccountAgreement", "");
//        para.put("isPlatformAgreement", "");
            para.put("name", AppGlobal.getApplyModel().getName());
            para.put("nationality", AppGlobal.getApplyModel().getNationality());
            para.put("nativePlace", AppGlobal.getApplyModel().getNativePlace());
            para.put("card", AppGlobal.getApplyModel().getCardNumber());
            para.put("gender", AppGlobal.getApplyModel().getGender());
            para.put("ethnic", AppGlobal.getApplyModel().getEthnic());
            para.put("birthday", AppGlobal.getApplyModel().getBirthday());
            para.put("address", AppGlobal.getApplyModel().getAddress());
            para.put("deliveryAddress", AppGlobal.getApplyModel().getDeliveryAddress());
//        para.put("status", "");
            para.put("logisticsCompany", "");
            para.put("shipmentNumber", "");
            para.put("use", AppGlobal.getApplyModel().getUse());
            para.put("email", AppGlobal.getApplyModel().getEmail());
            para.put("tel", AppGlobal.getApplyModel().getTel());
            para.put("postCode", AppGlobal.getApplyModel().getPostCode());
            para.put("career", AppGlobal.getApplyModel().getCareer());


            if (AppGlobal.getApplyModel().getIdCardImageFront() != null) {
                para.put("cardFrontPic", AppGlobal.getApplyModel().getIdCardImageFront().getFileUrl());
            }
            if (AppGlobal.getApplyModel().getIdCardImageBack() != null) {
                para.put("cardFollowingPic", AppGlobal.getApplyModel().getIdCardImageBack().getFileUrl());
            }
            if (AppGlobal.getApplyModel().getBankCardImage() != null) {
                para.put("bankCardPic", AppGlobal.getApplyModel().getIdCardImageBack().getFileUrl());
            }
            if (AppGlobal.getApplyModel().getFaceImage() != null) {
                para.put("headPic", AppGlobal.getApplyModel().getFaceImage().getFileUrl());
            }

            Retrofit retrofit = new RetrofitService().getRetrofit();
            APIInterface api = retrofit.create(APIInterface.class);

            Gson gson = new Gson();
            String strEntity = gson.toJson(para);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);

            Call<BaseModel<String>> call = api.applyCard(body);
            call.enqueue(new Callback<BaseModel<String>>() {
                @Override
                public void onResponse(Call<BaseModel<String>> call, Response<BaseModel<String>> response) {
                    try {
                        if (response != null || response.body() != null) {
                            if (BaseModel.SUCCESS.equals(response.body().status)) {
//                                ToastUtils.showCenter(FaceActivity.this, "申请成功！");
                                noLeakHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(BankCardScanActivity.this, SubmitApplyActivity.class);
                                    }
                                }, 2000);
                            } else {
                                ToastUtils.showCenter(BankCardScanActivity.this, response.message());
                            }
                        }
                    } catch (Exception e) {
                        Log.d("", e.getMessage());
                        ToastUtils.showCenter(BankCardScanActivity.this, e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                    Log.d("", t.getMessage());
                    ToastUtils.showCenter(BankCardScanActivity.this, t.getMessage());
                }
            });
        }
    }

}
