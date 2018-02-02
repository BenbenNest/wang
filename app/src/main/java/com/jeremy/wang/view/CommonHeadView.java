package com.jeremy.wang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremy.wang.R;
import com.jeremy.wang.activity.FlowActivity;

/**
 * Created by changqing on 2018/2/1.
 */

public class CommonHeadView extends RelativeLayout {
    ImageView mBackView;
    TextView mTitleView;
    OnBackListener onBackListener;

    public CommonHeadView(Context context) {
        super(context);
        init(context, null);
    }

    public CommonHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setOnBackListener(OnBackListener listener) {
        onBackListener = listener;
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.common_head_layout, this, true);
        RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mBackView = (ImageView) findViewById(R.id.iv_back);
        if (attrs != null) {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.CommonHeadView);
            int bgResId = typedArray.getResourceId(R.styleable.CommonHeadView_bg, 0);
            if (root != null && bgResId > 0) {
                root.setBackgroundResource(bgResId);
            }
            String title = typedArray.getString(R.styleable.CommonHeadView_title);
            mTitleView.setText(title);
            mBackView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBackListener != null) {
                        onBackListener.onBack();
                    }
                }
            });
            mTitleView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    FlowActivity.startActivity(context);
                }
            });
        }
    }

    public interface OnBackListener {
        void onBack();
    }

}
