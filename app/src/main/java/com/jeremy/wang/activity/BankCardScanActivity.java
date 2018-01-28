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
import com.jeremy.wang.utils.ToastUtils;
import com.jeremy.wang.view.CameraTopRectView;

public class BankCardScanActivity extends AppCompatActivity {
    CameraTopRectView cameraTopRectView;
    NoLeakHandler noLeakHandler;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BankCardScanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_scan);
        noLeakHandler = new NoLeakHandler(this);
        initActionBar();
        init();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("银行卡扫描");
        }
    }

    private void init() {
        cameraTopRectView = (CameraTopRectView) findViewById(R.id.rectOnCamera);
        cameraTopRectView.setTitle("请将银行卡放到方框中");
        Button btPic = (Button) findViewById(R.id.bt_pic);
        btPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showCenter(BankCardScanActivity.this, "申请已提交，请耐心等待！");
                noLeakHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HomeActivity.startActivity(BankCardScanActivity.this);
                    }
                }, 1000);

            }
        });
    }


}
