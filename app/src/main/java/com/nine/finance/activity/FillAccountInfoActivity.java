package com.nine.finance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.nine.finance.R;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.model.HomeInfo;
import com.nine.finance.utils.RegexUtils;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonInputLayout;

public class FillAccountInfoActivity extends BaseActivity {

    private ScrollView mScrollView;
    CommonInputLayout idInputLayout;
    CommonInputLayout nameInputLayout;
    CommonInputLayout sexInputLayout;
    CommonInputLayout ageInputLayout;
    CommonInputLayout nationInputLayout;
    CommonInputLayout homeInputLayout;
    CommonInputLayout phoneInputLayout;
    CommonInputLayout emailInputLayout;
    CommonInputLayout telInputLayout;
    CommonInputLayout useInputLayout;
    CommonInputLayout postCodeInputLayout;
    CommonInputLayout careerInputLayout;
    CommonInputLayout addressInputLayout;
    int REQUEST_CODE_HOME = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_account_info);
        initView();
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_HOME) {
                if (data != null) {
                    HomeInfo homeInfo = (HomeInfo) data.getSerializableExtra("home");
                    if (homeInfo != null && homeInfo.getName() != null) {
                        homeInputLayout.setText(homeInfo.getName());
                    }
                }
            }
        }
    }

    private void initView() {
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        idInputLayout = (CommonInputLayout) findViewById(R.id.id_input_layout);
        nameInputLayout = (CommonInputLayout) findViewById(R.id.name_input_layout);
        sexInputLayout = (CommonInputLayout) findViewById(R.id.sex_input_layout);
        ageInputLayout = (CommonInputLayout) findViewById(R.id.age_input_layout);
        nationInputLayout = (CommonInputLayout) findViewById(R.id.nation_input_layout);
        nationInputLayout.setText("中国");
        nationInputLayout.setEditable(false);
        homeInputLayout = (CommonInputLayout) findViewById(R.id.home_input_layout);
//        homeInputLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Intent intent = new Intent(FillAccountInfoActivity.this, HomeListActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_HOME);
//                return false;
//            }
//        });
        homeInputLayout.setOnFocusListener(new CommonInputLayout.OnFocusListener() {
            @Override
            public void onFocusListener() {
                Intent intent = new Intent(FillAccountInfoActivity.this, HomeListActivity.class);
                startActivityForResult(intent, REQUEST_CODE_HOME);
            }
        });
        homeInputLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(FillAccountInfoActivity.this, HomeListActivity.class);
                startActivityForResult(intent, REQUEST_CODE_HOME);
                return true;
            }
        });
        phoneInputLayout = (CommonInputLayout) findViewById(R.id.phone_input_layout);
        emailInputLayout = (CommonInputLayout) findViewById(R.id.email_input_layout);
        telInputLayout = (CommonInputLayout) findViewById(R.id.tel_input_layout);
        useInputLayout = (CommonInputLayout) findViewById(R.id.use_input_layout);
        postCodeInputLayout = (CommonInputLayout) findViewById(R.id.postcode_input_layout);
        careerInputLayout = (CommonInputLayout) findViewById(R.id.career_input_layout);
        addressInputLayout = (CommonInputLayout) findViewById(R.id.address_input_layout);
//        addressInputLayout.setActionMode(EditorInfo.IME_ACTION_NEXT);
        addressInputLayout.setActionDone(true);
        addressInputLayout.setOnFocusListener(new CommonInputLayout.OnFocusListener() {
            @Override
            public void onFocusListener() {
                if (mScrollView != null) {
                    mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    addressInputLayout.setOnFocusListener(null);
                    addressInputLayout.setFocus();
                }
            }
        });
        addressInputLayout.setOnActionDoneListener(new CommonInputLayout.OnActionDoneListener() {
            @Override
            public void onActionDone() {
                findViewById(R.id.bt_next).performClick();
            }
        });

        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillApplyInfo();
//                startActivity(FillAccountInfoActivity.this, FillMobileActivity.class);
                if (!checkInfo()) {
                    ToastUtils.showCenter(FillAccountInfoActivity.this, "请把信息填写完整！");
                    return;
                } else {
                    if (!RegexUtils.isMobile(telInputLayout.getText()) && !RegexUtils.isTelephone(telInputLayout.getText())) {
                        telInputLayout.requestFocus();
                        ToastUtils.showCenter(FillAccountInfoActivity.this, "请填写正确的手机号或者电话号码！");
                        return;
                    }
                    if (!RegexUtils.isMobile(phoneInputLayout.getText()) && !RegexUtils.isTelephone(phoneInputLayout.getText())) {
                        telInputLayout.requestFocus();
                        ToastUtils.showCenter(FillAccountInfoActivity.this, "请填写正确的手机号或者电话号码！");
                        return;
                    }
                    if (!RegexUtils.isPostCode(postCodeInputLayout.getText())) {
                        telInputLayout.requestFocus();
                        ToastUtils.showCenter(FillAccountInfoActivity.this, "请填写正确的邮编！");
                        return;
                    }
                    if (!RegexUtils.isEmail(emailInputLayout.getText())) {
                        telInputLayout.requestFocus();
                        ToastUtils.showCenter(FillAccountInfoActivity.this, "请填写正确的邮箱！");
                        return;
                    }
                }
                startActivity(FillAccountInfoActivity.this, FillMobileActivity.class);
            }
        });
    }

    private boolean checkInfo() {
        boolean flag = true;
        if (TextUtils.isEmpty(idInputLayout.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(nameInputLayout.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(sexInputLayout.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(ageInputLayout.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(nationInputLayout.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(homeInputLayout.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(phoneInputLayout.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(emailInputLayout.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(telInputLayout.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(useInputLayout.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(postCodeInputLayout.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(careerInputLayout.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(addressInputLayout.getText())) {
            return false;
        }
        return true;
    }

    private void fillApplyInfo() {
        AppGlobal.getApplyModel().setId(idInputLayout.getText());
        AppGlobal.getApplyModel().setAddress(addressInputLayout.getText());
        AppGlobal.getApplyModel().setDeliveryAddress(addressInputLayout.getText());
        AppGlobal.getApplyModel().setEthnic("");
        AppGlobal.getApplyModel().setGender(sexInputLayout.getText());
        AppGlobal.getApplyModel().setName(nameInputLayout.getText());
        AppGlobal.getApplyModel().setNationality(nationInputLayout.getText());
        AppGlobal.getApplyModel().setNativePlace(homeInputLayout.getText());
        AppGlobal.getApplyModel().setPhone(phoneInputLayout.getText());
//        AppGlobal.getApplyModel().setUserId(AppGlobal.getUserInfo().getUserId());
//        AppGlobal.getApplyModel().setBirthday();

//        AppGlobal.getApplyModel().mName = nameInputLayout.getText().toString().trim();
//        AppGlobal.getApplyModel().mGender = (sexInputLayout.getText().toString().trim().equals("男")) ? 1 : 2;
//        if (!TextUtils.isEmpty(ageInputLayout.getText().toString().trim())) {
//            AppGlobal.getApplyModel().mAge = Integer.valueOf(ageInputLayout.getText().toString().trim());
//        }
//        AppGlobal.getApplyModel().mNation = nationInputLayout.getText().toString().trim();
//        AppGlobal.getApplyModel().mHomeTown = homeInputLayout.getText().toString().trim();
//        AppGlobal.getApplyModel().mPhoneNum = phoneInputLayout.getText().toString().trim();
//        AppGlobal.getApplyModel().mEmail = emailInputLayout.getText().toString().trim();
//        AppGlobal.getApplyModel().mTelNum = telInputLayout.getText().toString().trim();
//        AppGlobal.getApplyModel().mAccountUse = useInputLayout.getText().toString().trim();
//        AppGlobal.getApplyModel().mPostCode = postCodeInputLayout.getText().toString().trim();
//        AppGlobal.getApplyModel().mCareer = careerInputLayout.getText().toString().trim();
//        AppGlobal.getApplyModel().mAddress = addressInputLayout.getText().toString().trim();
    }

    private void init() {

//        String address = jObject.getString("address");
//        String birthday = jObject.getString("birthday");
//        String gender = jObject.getString("gender");
//        Log.w("ceshi", "doOCR+++idCardBean.id_card_number===" + id_card_number + ", idCardBean.name===" + name);
//        String race = jObject.getString("race");
//        String side = jObject.getString("side");
//        JSONObject legalityObject = jObject.getJSONObject("legality");

        if (AppGlobal.mIDCardFront != null) {
            idInputLayout.setText(AppGlobal.mIDCardFront.optString("id_card_number"));
            nameInputLayout.setText(AppGlobal.mIDCardFront.optString("name"));
            sexInputLayout.setText(AppGlobal.mIDCardFront.optString("gender"));
            ageInputLayout.setText(AppGlobal.mIDCardFront.optString("birthday"));


        }
    }

}
