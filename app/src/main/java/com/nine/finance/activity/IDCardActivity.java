package com.nine.finance.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nine.finance.R;
import com.nine.finance.api.OnBooleanListener;
import com.nine.finance.idcard.AuthManager;

public class IDCardActivity extends BaseActivity implements AuthManager.AuthCallBack {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        init();
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

    @Override
    public void authState(boolean flag) {
        if (true) {
            startActivity(IDCardActivity.this, IDCardUploadActivity.class);
        }
    }
}
