package com.nine.finance.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.nine.finance.R;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonInputLayout;

public class SubmitApplyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_apply);


        findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonInputLayout mPasswordInputLayout = (CommonInputLayout) findViewById(R.id.password_input_layout);
                CommonInputLayout mPasswordAgainInputLayout = (CommonInputLayout) findViewById(R.id.password_again_input_layout);

                String pwd = mPasswordInputLayout.getText();
                String pwdAgain = mPasswordAgainInputLayout.getText();

                if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)) {
                    ToastUtils.showCenter(SubmitApplyActivity.this, "请输入密码");
                    return;
                }
                if (!pwd.equals(pwdAgain)) {
                    ToastUtils.showCenter(SubmitApplyActivity.this, "密码输入不一致");
                    return;
                }

                ToastUtils.showCenter(SubmitApplyActivity.this, "申请成功！");

                noLeakHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(SubmitApplyActivity.this, HomeActivity.class);
                    }
                }, 2000);
            }
        });
    }
}
