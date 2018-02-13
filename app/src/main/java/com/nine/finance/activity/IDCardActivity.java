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
import com.nine.finance.idcard.AuthManager;
import com.nine.finance.idcard.IDCardScanActivity;
import com.nine.finance.idcard.util.Screen;

public class IDCardActivity extends BaseActivity implements AuthManager.AuthCallBack {

    private ImageView mIvDirect;
    private ImageView mIvNonDirect;

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
        String path = getIntent().getStringExtra("path");
        boolean direct = getIntent().getBooleanExtra("direct", true);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (direct) {
            mIvDirect.setImageBitmap(bitmap);
        } else {
            mIvNonDirect.setImageBitmap(bitmap);
        }
    }

    private void init() {
        findViewById(R.id.id_direct).setOnClickListener(onClickListener);
        findViewById(R.id.id_direct_no).setOnClickListener(onClickListener);
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(IDCardActivity.this, FillAccountInfoActivity.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  // N以上的申请权限实例
//                    Log.d("MainActivity", "进入权限");
//                    onPermissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
//                        @Override
//                        public void onClick(boolean bln) {
//
//                            if (bln) {
//                                Log.d("MainActivity", "进入权限11");
//                                startActivity(IDCardActivity.this, FillAccountInfoActivity.class);
//                            } else {
//                                Toast.makeText(IDCardActivity.this, "扫码拍照或无法正常使用", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                } else {
//                    startActivity(IDCardActivity.this, FillAccountInfoActivity.class);
//                }
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
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
                startActivity(IDCardActivity.this, IDCardUploadActivity.class);
            }


        }
    };

    private static final int REQUEST_IDCARDSCAN_CODE = 100;

    @Override
    public void authState(boolean flag) {
        if (true) {
//            startActivity(IDCardActivity.this, IDCardScanActivity.class);
            Intent intent = new Intent(this, IDCardScanActivity.class);
            intent.putExtra("isvertical", true);
            intent.putExtra("isClearShadow", false);
            intent.putExtra("isTextDetect", false);
            intent.putExtra("isDebug", false);
            intent.putExtra("bound", 0.8);
            intent.putExtra("idcard", 0.1);
            intent.putExtra("clear", 0.8);
            startActivityForResult(intent, REQUEST_IDCARDSCAN_CODE);
        }
    }
}
