package com.nine.finance.activity;

import android.os.Bundle;
import android.view.View;

import com.nine.finance.R;
import com.nine.finance.permission.PermissionDialogUtils;
import com.nine.finance.permission.PermissionUtils;
import com.nine.finance.view.CommonHeadView;

import static com.nine.finance.permission.Permissions.REQUEST_CODE_CAMERA;

public class VerifyCodeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        commonHeadView = (CommonHeadView) findViewById(R.id.head_view);
        if (commonHeadView != null) {
            commonHeadView.setStep(R.drawable.step8);
        }
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                OpenglActivity.startActivity(VerifyCodeActivity.this);
//                startActivity(VerifyCodeActivity.this, SubmitApplyActivity.class);
//                FaceActivity.startActivity(VerifyCodeActivity.this);
                if (!PermissionUtils.checkSDPermission(VerifyCodeActivity.this)) {
                    PermissionUtils.requestSDAndCameraPermission(VerifyCodeActivity.this);
                } else {
                    FaceActivity.startActivity(VerifyCodeActivity.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE_CAMERA == requestCode) {
            if (!PermissionUtils.checkSDPermission(this) && !PermissionUtils.checkCameraPermission(this)) {
                PermissionDialogUtils.showSDAndCameraPermissionDialog(this);
            } else if (!PermissionUtils.checkSDPermission(this)) {
                PermissionDialogUtils.showSDPermissionDialog(this);
            } else if (!PermissionUtils.checkCameraPermission(this)) {
                PermissionDialogUtils.showCameraPermissionDialog(this);
            } else {
                FaceActivity.startActivity(VerifyCodeActivity.this);
            }
        }
    }


}
