package com.jeremy.wang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jeremy.wang.R;
import com.jeremy.wang.adapter.SpinnerAdapter;

public class ChooseBankTypeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bank_type);
        init();
    }

    private void init() {
        Spinner spinnerType = (Spinner) findViewById(R.id.spinner_banktype);
        String[] types = {"储蓄卡", "信用卡"};
        ArrayAdapter<String> adapter;
        adapter = new SpinnerAdapter(this, types);
        //设置下拉列表的风格,simple_spinner_dropdown_item是android系统自带的样式，等会自定义修改
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinnerType.setAdapter(adapter);

        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ChooseBankTypeActivity.this, VerifyCodeActivity.class);
            }
        });
    }


}
