package com.jeremy.wang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jeremy.wang.R;
import com.jeremy.wang.view.CommonInputLayout;

public class RegisterActivity extends AppCompatActivity {
    CommonInputLayout mIdInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        mIdInputLayout = findViewById(R.id.id_input_layout);
        mIdInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, IDCardUploadActivity.class);
                startActivity(intent);
            }
        });

    }


}
