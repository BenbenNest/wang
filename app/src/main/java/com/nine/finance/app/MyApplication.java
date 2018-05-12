package com.nine.finance.app;

import android.app.Application;

import com.nine.finance.business.UserManager;
import com.nine.finance.http.AppInfo;
import com.nine.finance.utils.CrashHandler;
import com.nine.finance.utils.KeyUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by changqing on 2018/2/3.
 */

public class MyApplication extends Application {
    static IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance(getApplicationContext()).init();
        AppGlobal.setAppContext(this);
        AppInfo.init(this);
        UserManager.getUserInfo(getApplicationContext());
        api = WXAPIFactory.createWXAPI(this, KeyUtil.WX_AppID, true);
        api.registerApp(KeyUtil.WX_AppID);
    }

    public static IWXAPI getWXAPI() {
        return api;
    }
}
