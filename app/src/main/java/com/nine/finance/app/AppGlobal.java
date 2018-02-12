package com.nine.finance.app;

import android.content.Context;

import com.nine.finance.model.UserInfo;
import com.nine.finance.model.UserLoginData;

/**
 * Created by jeremy
 */

public class AppGlobal {

    private static Context sAppContext = null;

    public static Context getAppContext() {
        return sAppContext;
    }

    public static void setAppContext(Context c) {
        sAppContext = c;
    }

    public static volatile UserInfo mUserInfo;

    public static volatile UserLoginData mUserLoginData;

}
