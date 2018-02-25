package com.nine.finance.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.nine.finance.R;
import com.nine.finance.constant.Constant;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonHeadView;
import com.nine.finance.view.MyWebView;

public class BankContractActivity extends BaseActivity {
    private CommonHeadView headView;
    private MyWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_contract);
        init();
    }

    private void init() {
        headView = (CommonHeadView) findViewById(R.id.head_view);
        webView = (MyWebView) findViewById(R.id.webview);
        webView.loadUrl(Constant.BANK_CONTRACT);

        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);

        final CheckBox chkAgree = (CheckBox) findViewById(R.id.chk_bank_contract);
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkAgree.isChecked()) {
                    startActivity(BankContractActivity.this, IDCardActivity.class);
                } else {
                    ToastUtils.showCenter(BankContractActivity.this, "请同意开户协议");
                }
            }
        });
    }

}

