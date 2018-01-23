package com.jeremy.wang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.wang.R;

/**
 * Created by changqing on 2018/1/23.
 */

public class BusinessRectView extends LinearLayout {
    ImageView mIvIcon;
    TextView mTvTitle;

    public BusinessRectView(Context context) {
        super(context);
        init(context, null);
    }

    public BusinessRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }


    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.business_rect_view, this, true);
        mIvIcon = findViewById(R.id.iv_icon);
        mTvTitle = findViewById(R.id.tv_title);
        if (attrs != null) {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.BusinessRectView);
            int icon = typedArray.getResourceId(R.styleable.BusinessRectView_icon, 0);
            if (mIvIcon != null) {
                mIvIcon.setImageResource(icon);
            }
            String title = typedArray.getString(R.styleable.BusinessRectView_text);
            if (mTvTitle != null) {
                mTvTitle.setText(title);
            }

        }

    }


}
