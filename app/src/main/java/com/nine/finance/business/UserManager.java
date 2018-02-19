package com.nine.finance.business;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.model.UserInfo;
import com.nine.finance.utils.GsonCore;
import com.nine.finance.utils.PreferenceUtils;

/**
 * Created by changqing on 2018/1/30.
 */

public class UserManager {
    public static final String KEY_LOGIN_INFO = "key_login_info";

    public static boolean checkLogin(Context context) {
        boolean flag = false;
        if (context != null) {
            UserInfo loginData = UserManager.getUserData(context);
            if (loginData != null && !TextUtils.isEmpty(loginData.getToken())) {
                AppGlobal.mUserLoginData = loginData;
                flag = true;
            }
        }
        return flag;
    }

    public static void saveUserData(Context context, UserInfo data) {
        if (context != null && data != null) {
            String loginfo = GsonCore.toJson(data);
            PreferenceUtils.saveData(context, KEY_LOGIN_INFO, loginfo);
        }
    }

    public static UserInfo getUserData(Context context) {
        UserInfo data = null;
        if (context != null) {
            String loginfo = PreferenceUtils.getData(context, KEY_LOGIN_INFO, "").toString();
            data = GsonCore.fromJson(loginfo, new TypeToken<UserInfo>() {
            }.getType());
        }
        return data;
    }

    public static void logOut(Context context) {
        AppGlobal.mUserLoginData.setToken(null);
        saveUserData(context, AppGlobal.mUserLoginData);
    }

    public static void updageLoginInfo() {

    }

}
