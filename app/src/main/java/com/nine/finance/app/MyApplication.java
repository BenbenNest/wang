package com.nine.finance.app;

import android.app.Application;

import com.nine.finance.http.AppInfo;
import com.nine.finance.utils.CrashHandler;

/**
 * Created by changqing on 2018/2/3.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance(getApplicationContext()).init();
        AppGlobal.setAppContext(this);
        AppInfo.init(this);
    }


}
