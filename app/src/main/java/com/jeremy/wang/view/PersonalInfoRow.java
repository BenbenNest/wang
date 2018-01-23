package com.jeremy.wang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jeremy.wang.R;

/**
 * Created by changqing on 2018/1/23.
 */

public class PersonalInfoRow extends LinearLayout {

    public PersonalInfoRow(Context context) {
        super(context);
        init(context, null);
    }

    public PersonalInfoRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.personal_info_row, this, true);
        if (attrs != null) {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.PersonalInfoRow);
            int icon = typedArray.getResourceId(R.styleable.PersonalInfoRow_icon, 0);
        }
    }


}
