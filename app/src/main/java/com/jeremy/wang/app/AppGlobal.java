package com.jeremy.wang.app;

import android.content.Context;

/**
 * Created by jeremy
 */

public class AppGlobal {

    private static Context sAppContext = null;

    public static Context getAppContext() { return sAppContext; }

    public static void setAppContext(Context c) { sAppContext = c; }
}
