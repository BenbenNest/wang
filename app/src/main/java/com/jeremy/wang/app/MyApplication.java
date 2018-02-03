package com.jeremy.wang.app;

import android.app.Application;

import com.jeremy.wang.utils.CrashHandler;

/**
 * Created by changqing on 2018/2/3.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance(getApplicationContext()).init();
    }


}
