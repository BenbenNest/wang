package com.jeremy.wang.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jeremy.wang.R;
import com.jeremy.wang.view.CommonButton;

public class PersonalInfoActivity extends AppCompatActivity {

    public static final int STEP_FIRST = 1;
    public static final int STEP_SECOND = 2;
    public static final int STEP_THIRD = 3;
    public static final int STEP_LAST = 4;

    private static int mCurrentStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        init();

    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_personal_info);

        CommonButton btNext = (CommonButton) findViewById(R.id.bt_next);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mCurrentStep) {
                    case STEP_FIRST:
                        IDCardUploadActivity.startActivity(PersonalInfoActivity.this);
                        break;
                    case STEP_SECOND:

                        break;
                    case STEP_THIRD:

                        break;
                    case STEP_LAST:

                        break;
                }
            }
        });

    }


}
