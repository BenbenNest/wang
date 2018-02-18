package com.nine.finance.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;

import com.nine.finance.R;
import com.nine.finance.constant.Constant;
import com.nine.finance.utils.ToastUtils;

public class BankContractActivity extends BaseActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_contract);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(Constant.BANK_CONTRACT);
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
