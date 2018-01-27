package com.jeremy.wang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.jeremy.wang.R;

/**
 * Created by changqing on 2018/1/27.
 */

public class CommonButton extends AppCompatTextView {
    public CommonButton(Context context) {
        super(context);
        init(context, null);
    }

    public CommonButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.CommonButton);
        if (typedArray != null) {

        }
    }


}
