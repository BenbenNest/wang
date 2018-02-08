package com.jeremy.wang.business;

import android.content.Context;

import com.jeremy.wang.app.AppGlobal;

/**
 * Created by changqing on 2018/1/30.
 */

public class UserManager {

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

    public static void updageLoginInfo() {

    }

}
