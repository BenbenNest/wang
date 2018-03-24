package com.nine.finance.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nine.finance.R;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.idcard.util.ConUtil;
import com.nine.finance.idcard.util.Util;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.ImageInfo;
import com.nine.finance.utils.KeyUtil;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FacePicActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIvFace;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    //调用照相机返回图片文件
    private File tempFile;
    String photoPath = "";

    public static void startActivity(Activity context) {
        Intent intent = new Intent(context, FacePicActivity.class);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, int requestCode) {
        Intent intent = new Intent(context, FacePicActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_pic);
        initView();
        getPicFromCamera();
    }

    private void initView() {
        mIvFace = (ImageView) findViewById(R.id.iv_face);
        findViewById(R.id.bt_recapture).setOnClickListener(this);
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoPath.equals("")) {
                    ToastUtils.showCenter(FacePicActivity.this, "请拍照");
                } else {
                    if (photoPath == null) {
                        ToastUtils.showCenter(FacePicActivity.this, "请安装SD卡");
                    } else {
                        uploadFile(photoPath);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_recapture:
                getPicFromCamera();
                break;
            default:
                break;
        }
    }

    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(FacePicActivity.this, "com.nine.finance", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:   //调用相机后返回
                if (resultCode == RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(FacePicActivity.this, "com.nine.finance", tempFile);
                        cropPhoto(contentUri);
                    } else {
                        cropPhoto(Uri.fromFile(tempFile));
                    }
                }
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    cropPhoto(uri);
                }
                break;
            case CROP_REQUEST_CODE:     //调用剪裁后返回
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
                    Bitmap image = bundle.getParcelable("data");
                    //设置到ImageView上
                    mIvFace.setImageBitmap(image);
                    //也可以进行一些保存、压缩等操作后上传
                    photoPath = saveImage("crop", image);
                }
                break;
        }
    }


    public void uploadFile(String path) {
        if (!NetUtil.isNetworkConnectionActive(FacePicActivity.this)) {
            ToastUtils.showCenter(FacePicActivity.this, getResources().getString(R.string.net_not_connect));
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
                    faceCompare();
                }
            }

            @Override
            public void onFailure(Call<BaseModel<ImageInfo>> call, Throwable t) {
                ToastUtils.showCenter(FacePicActivity.this, t.getMessage());
            }
        });
    }

    private void faceCompare() {
//        https://api-cn.faceplusplus.com/facepp/v3/compare
        String url = "https://api-cn.faceplusplus.com/facepp/v3/compare";
        RequestParams rParams = new RequestParams();
        Log.w("ceshi", "Util.API_OCRKEY===" + Util.API_SECRET + ", Util.API_OCRSECRET===" + Util.API_SECRET);
        rParams.put("api_key", KeyUtil.API_KEY);
        rParams.put("api_secret", KeyUtil.API_SECRET);
        String idCardPath = AppGlobal.getApplyModel().getIdCardImageFront().getFileUrl();
        String facePath = AppGlobal.getApplyModel().getFaceImage().getFileUrl();
        rParams.put("image_url1", idCardPath);
        rParams.put("image_url2", facePath);
        rParams.put("legality", 1 + "");
        AsyncHttpClient asyncHttpclient = new AsyncHttpClient();
        asyncHttpclient.post(url, rParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {
//                    showBar(false);
                try {
                    String successStr = new String(responseByte);
                    JSONObject jObject = new JSONObject(successStr);
                    double confidence = jObject.optDouble("confidence", 0);
                    if (confidence > 0) {
                        apply();
                    } else {
                        ConUtil.showToast(FacePicActivity.this, "人证合一验证失败！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                        showBar(false);
                    ConUtil.showToast(FacePicActivity.this, "人证合一验证失败！");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                if (responseBody != null) {
                    Log.w("ceshi", "responseBody===" + new String(responseBody));
                }
//                    showBar(false);
                ConUtil.showToast(FacePicActivity.this, "人证合一验证失败！");
            }
        });

    }

    private void apply() {
        if (!NetUtil.isNetworkConnectionActive(FacePicActivity.this)) {
            ToastUtils.showCenter(FacePicActivity.this, getResources().getString(R.string.net_not_connect));
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
                                        startActivity(FacePicActivity.this, SubmitApplyActivity.class);
                                    }
                                }, 2000);
                            } else {
                                ToastUtils.showCenter(FacePicActivity.this, response.message());
                            }
                        }
                    } catch (Exception e) {
                        Log.d("", e.getMessage());
                        ToastUtils.showCenter(FacePicActivity.this, e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<BaseModel<String>> call, Throwable t) {
                    Log.d("", t.getMessage());
                    ToastUtils.showCenter(FacePicActivity.this, t.getMessage());
                }
            });
        }
    }


    public String saveImage(String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
