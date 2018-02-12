package com.nine.finance.activity;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nine.finance.R;
import com.nine.finance.api.OnBooleanListener;
import com.nine.finance.thread.NoLeakHandler;
import com.nine.finance.view.CameraSurfaceView;

public class IDCardUploadActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, IDCardUploadActivity.class);
        context.startActivity(intent);
    }

    private Button button;
    private CameraSurfaceView mCameraSurfaceView;
    //    private RectOnCamera rectOnCamera;
    NoLeakHandler noLeakHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_upload);
        initActionBar();
        init();
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.IDScan);
        }
    }

    private void init() {
        noLeakHandler = new NoLeakHandler(this);

        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.cameraSurfaceView);
        button = (Button) findViewById(R.id.takePic);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPermissionRequests(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnBooleanListener() {
                    @Override
                    public void onClick(boolean bln) {
                        if (bln) {
                            mCameraSurfaceView.takePicture();
                            noLeakHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(IDCardUploadActivity.this, IDCardActivity.class);
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(IDCardUploadActivity.this, "文件读写或无法正常使用", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }


}
