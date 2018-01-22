package com.jeremy.wang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jeremy.wang.R;

/**
 * Created by changqing on 2018/1/22.
 */

public class CommonInputLayout extends LinearLayout {
    EditText mInputText;

    public CommonInputLayout(Context context) {
        super(context);
        init(context, null);
    }

    public CommonInputLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.common_input_layout, this, true);
        mInputText = findViewById(R.id.input_edit);
        if (attrs != null) {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.CommonInputLayout);
            String hint = typedArray.getString(R.styleable.CommonInputLayout_input_hint);
            if (mInputText != null) {
                mInputText.setText(hint);
            }
        }
    }


}
