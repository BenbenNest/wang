package com.nine.finance.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nine.finance.R;
import com.nine.finance.activity.FlowActivity;

/**
 * Created by changqing on 2018/2/1.
 */

public class CommonHeadView extends RelativeLayout {
    ImageView mStepView;
    ImageView mBackView;
    TextView mTitleView;
    TextView mActionView;
    OnBackListener onBackListener;
    OnActionListener onActionListener;

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
        mStepView = (ImageView) findViewById(R.id.iv_step);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mBackView = (ImageView) findViewById(R.id.iv_back);
        mActionView = (TextView) findViewById(R.id.tv_action);
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
            mActionView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionView != null) {
                        onActionListener.onAction();
                    }
                }
            });
        }
    }

    public void setTitle(String title) {
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    public void setAction(String text, OnActionListener listener) {
        onActionListener = listener;
        if (mActionView != null) {
            mActionView.setText(text);
        }
    }

    public void setStep(int resId) {
        if (mStepView != null) {
            mStepView.setImageResource(resId);
        }
    }

    public interface OnBackListener {
        void onBack();
    }

    public interface OnActionListener {
        void onAction();
    }

}
