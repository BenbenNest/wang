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
import android.widget.Button;
import android.widget.ImageView;

import com.nine.finance.R;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.business.UserManager;
import com.nine.finance.http.APIInterface;
import com.nine.finance.http.RetrofitService;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.ImageInfo;
import com.nine.finance.utils.NetUtil;
import com.nine.finance.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AvatarActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mHeader_iv;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    //调用照相机返回图片文件
    private File tempFile;
    String photoPath = "";

    public static void startActivityForResult(Activity context, int requestCode) {
        Intent intent = new Intent(context, AvatarActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);
        initView();
    }

    private void initView() {
        mHeader_iv = (ImageView) findViewById(R.id.iv_face);
        Button mGoCamera_btn = (Button) findViewById(R.id.bt_recapture);
        Button mGoAlbm_btn = (Button) findViewById(R.id.mGoAlbm_btn);
        mGoCamera_btn.setOnClickListener(this);
        mGoAlbm_btn.setOnClickListener(this);
        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoPath.equals("")) {
                    ToastUtils.showCenter(AvatarActivity.this, "请选择照片或者拍照");
                } else {
                    if (photoPath == null) {
                        ToastUtils.showCenter(AvatarActivity.this, "请安装SD卡");
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
            case R.id.mGoAlbm_btn:
                getPicFromAlbm();
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
            Uri contentUri = FileProvider.getUriForFile(AvatarActivity.this, "com.nine.finance", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            Log.e("dasd", contentUri.toString());
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
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
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
                        Uri contentUri = FileProvider.getUriForFile(AvatarActivity.this, "com.nine.finance", tempFile);
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
                    mHeader_iv.setImageBitmap(image);
                    //也可以进行一些保存、压缩等操作后上传
                    photoPath = saveImage("crop", image);
                }
                break;
        }
    }


    public void uploadFile(String path) {
        if (!NetUtil.isNetworkConnectionActive(AvatarActivity.this)) {
            ToastUtils.showCenter(AvatarActivity.this, getResources().getString(R.string.net_not_connect));
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
                    AppGlobal.getUserInfo().setHead(imageInfo.getFileUrl());
                    UserManager.saveUserData(AvatarActivity.this, AppGlobal.getUserInfo());
                    Intent intent = new Intent();
                    intent.putExtra("path", imageInfo.getFileUrl());
                    setResult(RESULT_OK, intent);
                    AvatarActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<BaseModel<ImageInfo>> call, Throwable t) {
                ToastUtils.showCenter(AvatarActivity.this, t.getMessage());
            }
        });
    }

    public String saveImage(String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (!appDir.exists()) {
            appDir.mkdirs();
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
