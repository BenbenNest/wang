package com.nine.finance.app;

import android.content.Context;

import com.nine.finance.model.ApplyModel;
import com.nine.finance.model.UserInfo;

import org.json.JSONObject;

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

    public static volatile UserInfo mUserLoginData;

    public static JSONObject mIDCardFront;

    public static JSONObject mIDCardBack;

    public static ApplyModel mApplyModel;

    public static ApplyModel getApplyModel() {
        if (mApplyModel == null) {
            mApplyModel = new ApplyModel();
        }
        return mApplyModel;
    }

}
