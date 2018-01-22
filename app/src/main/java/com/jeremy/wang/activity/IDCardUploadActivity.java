package com.jeremy.wang.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

//        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.cameraSurfaceView);
//        button = (Button) findViewById(R.id.takePic);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mCameraSurfaceView.takePicture();
//            }
//        });
    }



}
