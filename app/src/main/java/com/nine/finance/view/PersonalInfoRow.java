package com.nine.finance.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nine.finance.R;

/**
 * Created by changqing on 2018/1/23.
 */

public class PersonalInfoRow extends RelativeLayout {

    TextView title;
    EditText action;

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
            ImageView imageView = (ImageView) findViewById(R.id.icon);
            imageView.setImageResource(icon);
            title = (TextView) findViewById(R.id.title);
            action = (EditText) findViewById(R.id.action);
            String sTitle = typedArray.getString(R.styleable.PersonalInfoRow_title);
            title.setText(sTitle);
            String sAction = typedArray.getString(R.styleable.PersonalInfoRow_action);
            action.setText(sAction);
        }
    }

    public void setText(String text) {
        if (text != null) {
            action.setText(text);
        }
    }

    public String getText() {
        if (action != null) {
            return action.getText().toString().trim();
        }
        return "";
    }

    public void setEditable(boolean flag) {
        if (action != null) {
            action.setEnabled(flag);
        }
    }

}
