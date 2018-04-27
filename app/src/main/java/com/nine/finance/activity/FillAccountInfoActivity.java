package com.nine.finance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.nine.finance.R;
import com.nine.finance.adapter.SpinnerAdapter;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.model.HomeInfo;
import com.nine.finance.utils.RegexUtils;
import com.nine.finance.utils.ToastUtils;
import com.nine.finance.view.CommonHeadView;
import com.nine.finance.view.CommonInputLayout;

public class FillAccountInfoActivity extends BaseActivity {

    private ScrollView mScrollView;
    Spinner spinner;
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
        commonHeadView = (CommonHeadView) findViewById(R.id.head_view);
        if (commonHeadView != null) {
            commonHeadView.setStep(R.drawable.step4);
        }
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        idInputLayout = (CommonInputLayout) findViewById(R.id.id_input_layout);
        idInputLayout.setEditable(false);
        nameInputLayout = (CommonInputLayout) findViewById(R.id.name_input_layout);
        nameInputLayout.setEditable(false);
        sexInputLayout = (CommonInputLayout) findViewById(R.id.sex_input_layout);
        ageInputLayout = (CommonInputLayout) findViewById(R.id.age_input_layout);
        ageInputLayout.setEditable(false);
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
        phoneInputLayout.requestFocus();
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
                        phoneInputLayout.requestFocus();
                        ToastUtils.showCenter(FillAccountInfoActivity.this, "请填写正确的手机号或者电话号码！");
                        return;
                    }
                    if (!RegexUtils.isPostCode(postCodeInputLayout.getText())) {
                        postCodeInputLayout.requestFocus();
                        ToastUtils.showCenter(FillAccountInfoActivity.this, "请填写正确的邮编！");
                        return;
                    }
                    if (!RegexUtils.isEmail(emailInputLayout.getText())) {
                        emailInputLayout.requestFocus();
                        ToastUtils.showCenter(FillAccountInfoActivity.this, "请填写正确的邮箱！");
                        return;
                    }
                }
                startActivity(FillAccountInfoActivity.this, FillMobileActivity.class);
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);
        final String[] uses = {"开户用途", "理财", "消费", "结算"};
        AppGlobal.getApplyModel().setUse(uses[0]);
        ArrayAdapter<String> adapter;
        adapter = new SpinnerAdapter(this, uses);
        //设置下拉列表的风格,simple_spinner_dropdown_item是android系统自带的样式，等会自定义修改
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    AppGlobal.getApplyModel().setUse(uses[position]);
                } else {
                    AppGlobal.getApplyModel().setUse("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
//        if (TextUtils.isEmpty(AppGlobal.getApplyModel().getUse())) {
//            return false;
//        }
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
        AppGlobal.getApplyModel().setIdCard(idInputLayout.getText());
        AppGlobal.getApplyModel().setAddress(addressInputLayout.getText());
        AppGlobal.getApplyModel().setDeliveryAddress(addressInputLayout.getText());
        AppGlobal.getApplyModel().setEthnic("");
        AppGlobal.getApplyModel().setGender(sexInputLayout.getText());
        AppGlobal.getApplyModel().setName(nameInputLayout.getText());
        AppGlobal.getApplyModel().setNationality(nationInputLayout.getText());
        AppGlobal.getApplyModel().setNativePlace(homeInputLayout.getText());
        AppGlobal.getApplyModel().setPhone(phoneInputLayout.getText());
//        AppGlobal.getApplyModel().setUserId(AppGlobal.getUserInfo().getUserId());
//        AppGlobal.getApplyModel().setBirthday(AppGlobal.getUserInfo().getBirthday());
        AppGlobal.getApplyModel().setEmail(emailInputLayout.getText());
        AppGlobal.getApplyModel().setTel(telInputLayout.getText());
        AppGlobal.getApplyModel().setPostCode(postCodeInputLayout.getText());
        AppGlobal.getApplyModel().setCareer(careerInputLayout.getText());
        RadioButton rdFinance = (RadioButton) findViewById(R.id.rd_finance);
        RadioButton rdConsume = (RadioButton) findViewById(R.id.rd_consume);
        RadioButton rdSettleAccount = (RadioButton) findViewById(R.id.rd_settle_account);
        if (rdFinance.isChecked()) {
            AppGlobal.getApplyModel().setUse(rdFinance.getText().toString());
        } else if (rdConsume.isChecked()) {
            AppGlobal.getApplyModel().setUse(rdConsume.getText().toString());
        } else {
            AppGlobal.getApplyModel().setUse(rdSettleAccount.getText().toString());
        }
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
