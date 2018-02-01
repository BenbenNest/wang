package com.jeremy.wang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.jeremy.wang.R;

public class ChooseBankActivity extends BaseActivity {

    private AppCompatSpinner spinnerBank;
    private AppCompatSpinner spinnerAddress;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChooseBankActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bank);
        init();
    }

    private void init() {
        setTitle("选择开户银行");
        spinnerBank = (AppCompatSpinner) findViewById(R.id.spinner_bank);
        spinnerAddress = (AppCompatSpinner) findViewById(R.id.spinner_address);
        String[] name = {"中国银行", "工商银行", "建设银行", "农业银行", "北京银行"};
        String[] address = {"中国银行西三旗支行", "工商银行国贸支行", "建设银行昌平支行", "农业银行五道口支行", "北京银行西三旗支行"};
        ArrayAdapter<String> adapter;
//        adapter = ArrayAdapter.createFromResource(this, R.array.songs, android.R.layout.simple_spinner_item);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, name);
        //设置下拉列表的风格,simple_spinner_dropdown_item是android系统自带的样式，等会自定义修改
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinnerBank.setAdapter(adapter);
        //添加事件Spinner事件监听
        spinnerBank.setOnItemSelectedListener(new SpinnerSelectedListener());

        ArrayAdapter<String> adapterAddress = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, address);
        adapterAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerAddress.setAdapter(adapterAddress);

        findViewById(R.id.tv_intro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ChooseBankActivity.this, BankIntroActivity.class);
            }
        });
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ChooseBankActivity.this, BankContractActivity.class);
            }
        });
    }

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//            text.setText("我的名字是："+name[arg2]);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

}
