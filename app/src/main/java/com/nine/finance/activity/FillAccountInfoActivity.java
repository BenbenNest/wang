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
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FillAccountInfoActivity.this, FillMobileActivity.class);
            }
        });
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
