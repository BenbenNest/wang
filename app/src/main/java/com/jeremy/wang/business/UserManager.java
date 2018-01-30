package com.jeremy.wang.business;

import android.content.Context;
import android.text.TextUtils;

import com.jeremy.wang.utils.Constant;
import com.jeremy.wang.utils.PreferenceUtils;

/**
 * Created by changqing on 2018/1/30.
 */

public class UserManager {

    public static boolean checkLogin(Context context) {
        boolean flag = false;
        String id = PreferenceUtils.getData(context, Constant.PREFERENCE_ACCOUNT_ID, "").toString();
        String pwd = PreferenceUtils.getData(context, Constant.PREFERENCE_ACCOUNT_PWD, "").toString();
        if (!TextUtils.isEmpty(id) && TextUtils.isEmpty(pwd)) {
            flag = true;
        }
        return flag;
    }

}
