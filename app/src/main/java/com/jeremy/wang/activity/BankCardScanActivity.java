package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jeremy.wang.R;

public class BankCardScanActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BankCardScanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_scan);

        init();
    }

    private void init() {
        Button btPic = (Button) findViewById(R.id.bt_pic);
        btPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.startActivity(BankCardScanActivity.this);
            }
        });
    }


}
