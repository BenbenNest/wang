package com.nine.finance.business;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.google.gson.reflect.TypeToken;
import com.nine.finance.app.AppGlobal;
import com.nine.finance.http.ServiceHttpConfig;
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
                AppGlobal.mUserInfo = loginData;
                flag = true;
            }
        }
        return flag;
    }

    public static UserInfo getUserInfo(Context context) {
        UserInfo userInfo = null;
        userInfo = UserManager.getUserData(context);
        AppGlobal.setUserInfo(userInfo);
        return userInfo;
    }

    public static void saveUserData(Context context, UserInfo data) {
        if (context != null) {
            if (data != null) {
                String loginfo = GsonCore.toJson(data);
                PreferenceUtils.saveData(context, KEY_LOGIN_INFO, loginfo);
                saveUserCookie(context, data);
            } else {
                PreferenceUtils.saveData(context, KEY_LOGIN_INFO, "");
            }
        }
    }

    public static void saveUserCookie(Context context, UserInfo data) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        String url = ServiceHttpConfig.getHost();
        cookieManager.setCookie(url, "UserId=" + data.getUserId());
        cookieManager.setCookie(url, "Token=" + data.getToken());
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }

    public static UserInfo getUserData(Context context) {
        UserInfo data = null;
        if (context != null) {
            String loginfo = PreferenceUtils.getData(context, KEY_LOGIN_INFO, "").toString();
            if (TextUtils.isEmpty(loginfo)) {
                data = null;
            } else {
                data = GsonCore.fromJson(loginfo, new TypeToken<UserInfo>() {
                }.getType());
            }
        }
        return data;
    }

    public static void logOut(Context context) {
        if (AppGlobal.getUserInfo() != null) {
            AppGlobal.setUserInfo(null);
            saveUserData(context, null);
        }
        ((Activity) context).finish();
    }

    public static void updageLoginInfo() {

    }

}
