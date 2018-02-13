package com.nine.finance.business;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.model.UserLoginData;
import com.nine.finance.utils.GsonCore;
import com.nine.finance.utils.PreferenceUtils;

/**
 * Created by changqing on 2018/1/30.
 */

public class UserManager {
    public static final String KEY_LOGIN_INFO = "key_login_info";

    public static boolean checkLogin(Context context) {
        //全局静态变量保存登录信息，一旦应用被杀死，需要重新登录
        boolean flag = false;
//        String id = PreferenceUtils.getData(context, Constant.PREFERENCE_ACCOUNT_ID, "").toString();
//        String pwd = PreferenceUtils.getData(context, Constant.PREFERENCE_ACCOUNT_PWD, "").toString();
//        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(pwd)) {
//            flag = true;
//        }
        if (AppGlobal.mUserLoginData != null) {
            flag = true;
        }
        return flag;
    }

    public static void saveUserData(Context context, UserLoginData data) {
        if (context != null && data != null) {
            String loginfo = GsonCore.toJson(data);
            PreferenceUtils.saveData(context, KEY_LOGIN_INFO, loginfo);
        }
    }

    public static UserLoginData getUserData(Context context) {
        UserLoginData data = null;
        if (context != null) {
            String loginfo = PreferenceUtils.getData(context, KEY_LOGIN_INFO, "").toString();
            data = GsonCore.fromJson(loginfo, new TypeToken<UserLoginData>() {
            }.getType());
        }
        return data;
    }

    public static void updageLoginInfo() {

    }

}
