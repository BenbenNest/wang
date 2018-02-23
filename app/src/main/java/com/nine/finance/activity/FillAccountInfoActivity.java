package com.nine.finance.activity;

import android.os.Bundle;
import android.view.View;

import com.nine.finance.R;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.view.CommonInputLayout;

public class FillAccountInfoActivity extends BaseActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_account_info);
        initView();
        init();
    }

    private void initView() {
        idInputLayout = (CommonInputLayout) findViewById(R.id.id_input_layout);
        nameInputLayout = (CommonInputLayout) findViewById(R.id.name_input_layout);
        sexInputLayout = (CommonInputLayout) findViewById(R.id.sex_input_layout);
        ageInputLayout = (CommonInputLayout) findViewById(R.id.age_input_layout);
        nationInputLayout = (CommonInputLayout) findViewById(R.id.nation_input_layout);
        homeInputLayout = (CommonInputLayout) findViewById(R.id.home_input_layout);

        phoneInputLayout = (CommonInputLayout) findViewById(R.id.phone_input_layout);
        emailInputLayout = (CommonInputLayout) findViewById(R.id.email_input_layout);
        telInputLayout = (CommonInputLayout) findViewById(R.id.tel_input_layout);
        useInputLayout = (CommonInputLayout) findViewById(R.id.use_input_layout);
        postCodeInputLayout = (CommonInputLayout) findViewById(R.id.postcode_input_layout);
        careerInputLayout = (CommonInputLayout) findViewById(R.id.career_input_layout);
        addressInputLayout = (CommonInputLayout) findViewById(R.id.address_input_layout);

        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillApplyInfo();
                startActivity(FillAccountInfoActivity.this, FillMobileActivity.class);
            }
        });
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

        }
    }

}
