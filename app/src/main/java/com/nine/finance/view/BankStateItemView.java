package com.nine.finance.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nine.finance.R;

/**
 * Created by changqing on 2018/1/27.
 */

public class BankStateItemView extends RelativeLayout {

    public BankStateItemView(Context context) {
        super(context);
        init(context, null);
    }

    public BankStateItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.bank_list_item_layout, this, true);
        TextView tvBankName = findViewById(R.id.bank_name);
        ImageView btAction = findViewById(R.id.bt_action);
        btAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (attrs != null) {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.BankListItemView);
            String bankName = typedArray.getString(R.styleable.BankListItemView_bank_name);
            String cardNum = typedArray.getString(R.styleable.BankListItemView_card_num);
            int resId = typedArray.getResourceId(R.styleable.BankListItemView_icon, 0);
            if (!TextUtils.isEmpty(bankName)) {
                tvBankName.setText(bankName);
            }
//            if (!TextUtils.isEmpty(cardNum)) {
//                tvCardNum.setText(cardNum);
//            }
            if (resId > 0) {
                btAction.setImageResource(resId);
            }
        }
    }

}
