package com.nine.finance.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nine.finance.R;
import com.nine.finance.api.OnBooleanListener;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.idcard.AuthManager;
import com.nine.finance.view.CommonInputLayout;

public class BindBankCardActivity extends BaseActivity implements AuthManager.AuthCallBack {
    CommonInputLayout nameInputLayout;
    CommonInputLayout bankcardInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank_card);
        init();
    }

    private void init() {
        nameInputLayout = (CommonInputLayout) findViewById(R.id.name_input_layout);
        if (AppGlobal.getApplyModel() != null) {
            nameInputLayout.setText(AppGlobal.getApplyModel().getName());
        }
//        nameInputLayout.setText(AppGlobal().getName());
        bankcardInputLayout = (CommonInputLayout) findViewById(R.id.bankcard_input_layout);
        bankcardInputLayout.setOnClickListener(new BankCardOnclickListener(true));
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppGlobal.getApplyModel().setCardNumber(bankcardInputLayout.getText().toString().trim());
                startActivity(BindBankCardActivity.this, ChooseBankTypeActivity.class);
            }
        });
        findViewById(R.id.bt_scan).setOnClickListener(new BankCardOnclickListener(true));
    }

    class BankCardOnclickListener implements View.OnClickListener {
        public BankCardOnclickListener(boolean direct) {
//            mIsDirect = direct;
        }

        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  // N以上的申请权限实例
                Log.d("MainActivity", "进入权限");
                onPermissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
                    @Override
                    public void onClick(boolean bln) {
                        if (bln) {
                            AuthManager.checkIDCardAuthState(BindBankCardActivity.this, BindBankCardActivity.this);
                        } else {
                            Toast.makeText(BindBankCardActivity.this, "扫码拍照或无法正常使用", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(BindBankCardActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, 10);
                        }
                    }
                });
            } else {
                startActivity(BindBankCardActivity.this, BankCardScanActivity.class);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SCAN_CODE && data != null) {
                String path = data.getStringExtra("path");
                String IDCardInfo = data.getStringExtra("info");
                Bitmap bitmap = BitmapFactory.decodeFile(path);
            }
        }

    }

    private static final int REQUEST_SCAN_CODE = 100;

    @Override
    public void authState(boolean flag) {
        if (true) {
//            startActivity(IDCardActivity.this, IDCardScanActivity.class);
            Intent intent = new Intent(this, BankCardScanActivity.class);
            intent.putExtra("isvertical", true);
            intent.putExtra("isClearShadow", false);
            intent.putExtra("isTextDetect", false);
            intent.putExtra("isDebug", false);
            intent.putExtra("bound", 0.8);
            intent.putExtra("idcard", 0.1);
            intent.putExtra("clear", 0.8);
            startActivityForResult(intent, REQUEST_SCAN_CODE);
        }

    }
}
