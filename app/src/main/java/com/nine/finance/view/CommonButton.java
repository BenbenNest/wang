package com.nine.finance.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nine.finance.R;

/**
 * Created by changqing on 2018/1/27.
 */

public class CommonButton extends LinearLayout {
    TextView btAction;

    public CommonButton(Context context) {
        super(context);
        init(context, null);
    }

    public CommonButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.common_button_layout, this, true);
        btAction = (TextView) findViewById(R.id.tv_action);
        if (attrs != null) {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.CommonButton);
            if (typedArray != null) {
                String title = typedArray.getString(R.styleable.CommonButton_title);
                btAction.setText(title);
            }
        }

    }


}
