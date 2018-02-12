package com.nine.finance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nine.finance.R;
import com.nine.finance.view.CommonButton;

public class EditPersonalInfoActivity extends AppCompatActivity {
    public static final int STEP_FIRST = 1;
    public static final int STEP_SECOND = 2;
    public static final int STEP_THIRD = 3;
    public static final int STEP_LAST = 4;

    private static int mCurrentStep = 1;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EditPersonalInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        initActionBar();
        init();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("开户申请");
        }
    }

    private void init() {
        CommonButton bt_next = (CommonButton) findViewById(R.id.bt_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mCurrentStep) {
                    case STEP_FIRST:
                        IDCardUploadActivity.startActivity(EditPersonalInfoActivity.this);
                        break;
                    case STEP_SECOND:
                        BankCardScanActivity.startActivity(EditPersonalInfoActivity.this);
                        break;
                    case STEP_THIRD:
                        //提交申请
//                        submit();
                        HomeActivity.startActivity(EditPersonalInfoActivity.this);
                        break;
                    case STEP_LAST:

                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public static void setCurrentStep(int step) {
        mCurrentStep = step;
    }


}
