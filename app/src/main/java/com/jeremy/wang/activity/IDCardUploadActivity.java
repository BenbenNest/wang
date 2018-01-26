package com.jeremy.wang.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jeremy.wang.R;
import com.jeremy.wang.view.CameraSurfaceView;

public class IDCardUploadActivity extends AppCompatActivity {

    private Button button;
    private CameraSurfaceView mCameraSurfaceView;
//    private RectOnCamera rectOnCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_upload);
        init();
        mCameraSurfaceView = findViewById(R.id.cameraSurfaceView);
        button = (Button) findViewById(R.id.takePic);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraSurfaceView.takePicture();
            }
        });
    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.IDScan);
        }
    }


}
