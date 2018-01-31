package com.jeremy.wang.activity;

import android.os.Bundle;

import com.jeremy.wang.R;

public class PersonalInfoActivity extends BaseActivity {

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
        setTitle(R.string.title_personal_info);



    }


}
