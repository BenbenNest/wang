package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jeremy.wang.R;

public class FaceScanActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FaceScanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_scan);
        initActionBar();
        init();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("人脸图像识别");
        }
    }

    private void init() {
        Button bt_pic = (Button) findViewById(R.id.bt_pic);
        bt_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaceScanActivity.this, EditPersonalInfoActivity.class);
                startActivity(intent);
            }
        });
    }


}
