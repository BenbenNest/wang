package com.nine.finance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.nine.finance.R;
import com.nine.finance.activity.bank.BankListActivity;
import com.nine.finance.activity.bank.BranchListActivity;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.constant.Constant;
import com.nine.finance.model.ApplyModel;
import com.nine.finance.model.BankInfo;

public class ChooseBankActivity extends BaseActivity {

    private AppCompatSpinner spinnerBank;
    private AppCompatSpinner spinnerAddress;
    private EditText mEditBankView;
    private EditText mEditBranchView;

    private static final int REQUEST_CODE_BANK = 1001;
    private static final int REQUEST_CODE_BRANCH = 1002;

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
        AppGlobal.mApplyModel = new ApplyModel();
//        testSpinner();
        mEditBankView = (EditText) findViewById(R.id.et_bank);
        mEditBankView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseBankActivity.this, BankListActivity.class);
                startActivityForResult(intent, REQUEST_CODE_BANK);
            }
        });
        mEditBranchView = (EditText) findViewById(R.id.et_branch);
        mEditBranchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseBankActivity.this, BranchListActivity.class);
                startActivityForResult(intent, REQUEST_CODE_BRANCH);
            }
        });

        findViewById(R.id.tv_intro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(ChooseBankActivity.this, BankIntroActivity.class);
                WebViewActivity.startActivity(ChooseBankActivity.this, "银行介绍", Constant.BANK_INTRO);
            }
        });
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ChooseBankActivity.this, BankContractActivity.class);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_BANK) {
                if (data != null) {
                    BankInfo bankInfo = (BankInfo) data.getSerializableExtra("bank");
                    if (bankInfo != null && mEditBankView != null) {
                        mEditBankView.setText(bankInfo.getBankName());
                    }
                }
            } else if (requestCode == REQUEST_CODE_BRANCH) {
                if (data != null) {
                    BankInfo bankInfo = (BankInfo) data.getSerializableExtra("branch");
                    if (bankInfo != null && mEditBankView != null) {
                        mEditBranchView.setText(bankInfo.getBankName());
                    }
                }
            }
        }
    }

    private void testSpinner() {
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
