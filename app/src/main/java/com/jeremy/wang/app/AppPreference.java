package com.jeremy.wang.app;

import android.content.Context;

import com.jeremy.wang.utils.PreferenceUtils;

/**
 * Created by changqing on 2018/2/9.
 */

public class AppPreference {

    //          PreferenceUtils.saveData(this, Constant.PREFERENCE_ACCOUNT_ID, id);
//        PreferenceUtils.saveData(this, Constant.PREFERENCE_ACCOUNT_PWD, pwd);
    public static final String KEY_REMEMBER_USER_NAME = "key_remember_user_name";

    public static void setRememberUserName(Context context, boolean flag) {
        PreferenceUtils.saveData(context, KEY_REMEMBER_USER_NAME, flag);
    }

}
