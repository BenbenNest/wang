package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jeremy.wang.R;
import com.jeremy.wang.thread.NoLeakHandler;
import com.jeremy.wang.view.CameraSurfaceView;

public class IDCardUploadActivity extends AppCompatActivity {

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
        ActionBar actionBar = getSupportActionBar();
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
                mCameraSurfaceView.takePicture();
                noLeakHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EditPersonalInfoActivity.setCurrentStep(EditPersonalInfoActivity.STEP_SECOND);
                        EditPersonalInfoActivity.startActivity(IDCardUploadActivity.this);
                    }
                }, 1000);
            }
        });
    }


}
