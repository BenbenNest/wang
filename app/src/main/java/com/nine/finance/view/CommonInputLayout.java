package com.nine.finance.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nine.finance.R;

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
            boolean isScan = typedArray.getBoolean(R.styleable.CommonInputLayout_input_scan, false);
//            boolean isShowPwd = typedArray.getBoolean(R.styleable.CommonInputLayout_show_pwd, false);
            if (mInputText != null) {
                mInputText.setHint(hint);
                mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT && actionDone) {
                            if (onActionDoneListener != null) {
                                onActionDoneListener.onActionDone();
                            }
                        }
                        return false;
                    }
                });
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
            if (isScan) {
                ImageView iv_pwd = (ImageView) findViewById(R.id.bt_action);
                iv_pwd.setBackgroundResource(R.drawable.scan);
                iv_pwd.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnScanListener != null) {
                            mOnScanListener.onScan();
                        }
                    }
                });
            }
        }
    }

    public void setEditable(boolean flag) {
        if (mInputText != null) {
            mInputText.setEnabled(flag);
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
            return mInputText.getText().toString().trim();
        }
        return "";
    }

    public void setText(String text) {
        if (mInputText != null) {
            mInputText.setText(text);
        }
    }


    OnFocusListener onFocusListener;

    public void setOnFocusListener(OnFocusListener listener) {
        onFocusListener = listener;
        if (mInputText != null) {
            mInputText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        if (onFocusListener != null) {
                            onFocusListener.onFocusListener();
                        }
                    }
                }
            });
        }
    }

    OnActionDoneListener onActionDoneListener;

    public void setOnActionDoneListener(OnActionDoneListener listener) {
        onActionDoneListener = listener;
    }

    TextWatcher textWatcher;

    public void setOnTextChangeListener(TextWatcher watcher) {
        textWatcher = watcher;
        if (mInputText != null) {
            mInputText.addTextChangedListener(watcher);
        }
    }

    public void setFocus() {
        if (mInputText != null) {
            mInputText.requestFocus();
            mInputText.requestFocusFromTouch();
        }
    }

    public void setActionMode(int mode) {
        if (mInputText != null) {
            mInputText.setImeOptions(mode);
        }
    }

    boolean actionDone = false;

    public void setActionDone(boolean flag) {
        actionDone = flag;
    }

    public interface OnFocusListener {
        void onFocusListener();
    }

    public interface OnActionDoneListener {
        void onActionDone();
    }

    OnScanListener mOnScanListener;

    public void setOnScanListener(OnScanListener listener) {
        mOnScanListener = listener;
    }

    public interface OnScanListener {
        void onScan();
    }

}
