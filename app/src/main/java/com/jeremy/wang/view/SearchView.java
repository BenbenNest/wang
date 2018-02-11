package com.jeremy.wang.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jeremy.wang.R;

/**
 * Created by changqing on 2018/2/11.
 */

public class SearchView extends LinearLayout {
    public SearchView(Context context) {
        super(context);
        init(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.search_view_layout, this, true);
//        TextView tvBankName = findViewById(R.id.bank_name);
//        ImageView btAction = findViewById(R.id.bt_action);
//        btAction.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        if (attrs != null) {
//            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.BankListItemView);
//            String bankName = typedArray.getString(R.styleable.BankListItemView_bank_name);
//            String cardNum = typedArray.getString(R.styleable.BankListItemView_card_num);
//            int resId = typedArray.getResourceId(R.styleable.BankListItemView_icon, 0);
//            if (!TextUtils.isEmpty(bankName)) {
//                tvBankName.setText(bankName);
//            }
////            if (!TextUtils.isEmpty(cardNum)) {
////                tvCardNum.setText(cardNum);
////            }
//            if (resId > 0) {
//                btAction.setImageResource(resId);
//            }
//        }
    }


}
