package com.jeremy.wang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jeremy.wang.R;

/**
 * Created by changqing on 2018/1/22.
 */

public class CommonInputLayout extends LinearLayout {
    ImageView mIcon;
    EditText mInputText;

    private static boolean pwdStatus = false;

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
        mIcon = findViewById(R.id.iv_icon);
        mInputText = findViewById(R.id.input_edit);
        if (attrs != null) {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.CommonInputLayout);
            int resId = typedArray.getResourceId(R.styleable.CommonInputLayout_input_drawable, 0);
            mIcon.setBackgroundResource(resId);
            String hint = typedArray.getString(R.styleable.CommonInputLayout_input_hint);
            boolean isPwd = typedArray.getBoolean(R.styleable.CommonInputLayout_input_pwd, false);
//            boolean isShowPwd = typedArray.getBoolean(R.styleable.CommonInputLayout_show_pwd, false);
            if (mInputText != null) {
                mInputText.setHint(hint);
            }
            if (isPwd) {
                pwdStatus = true;
                mInputText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ImageView iv_pwd = (ImageView) findViewById(R.id.bt_action);
                iv_pwd.setBackgroundResource(R.drawable.pwd_control);
                iv_pwd.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        togglePwdState();
                    }
                });
            }
        }
    }

    private void togglePwdState() {
        if (pwdStatus) {
            pwdStatus = false;
            mInputText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            pwdStatus = true;
            mInputText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    public String getText() {
        if (mInputText != null) {
            return mInputText.getText().toString();
        }
        return "";
    }

    public void setText(String text) {
        if (mInputText != null) {
            mInputText.setText(text);
        }
    }

}
