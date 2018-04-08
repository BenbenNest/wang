package com.nine.finance.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nine.finance.R;

/**
 * Created by changqing on 2018/2/11.
 */

public class SearchView extends LinearLayout {
    private ImageView ivSearch;
    private EditText etContent;

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
        etContent = (EditText) findViewById(R.id.et_content);
        ivSearch = (ImageView) findViewById(R.id.iv_search);

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

    public void setOnTextChangeLitener(TextWatcher textWatcher) {
        if (etContent != null) {
            etContent.addTextChangedListener(textWatcher);
        }
    }

    public void setSearchListener(OnClickListener onClickListener) {
        if (ivSearch != null) {
            ivSearch.setOnClickListener(onClickListener);
        }
    }

    public String getKey() {
        String key = "";
        if (etContent != null) {
            key = etContent.getText().toString().trim();
        }
        return key;
    }


}
