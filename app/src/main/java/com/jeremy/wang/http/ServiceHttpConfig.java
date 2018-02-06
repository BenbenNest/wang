package com.jeremy.wang.http;

import android.content.Context;
import android.support.annotation.NonNull;

public class ServiceHttpConfig {

    public static final int TYPE_SERVER_OFFICIAL = 0;
    public static final int TYPE_SERVER_TEST = 1;
    public static final int TYPE_SERVER_DEV = 2;
    public static final int TYPE_SERVER_STAGING = 3;

    public static int SERVER_TYPE = TYPE_SERVER_DEV;

    private static final String SERVER_OFFICIAL = "https://app.nextev.com";
    private static final String SERVER_AWS_TEST = "https://app-test.nextev.com";
    private static final String SERVER_AWS_DEV = "http://39.106.173.14:8080";
    private static final String SERVER_AWS_STAGING = "https://app-staging.nextev.com";

    public static final String NEXTEV_APP_STORE_HOST_TEST = "http://10.110.1.55:8081";//APPStore域名未备好之前使用
    public static final String NEXTEV_APP_STORE_HOST = "http://nextevapp.chinacloudapp.cn";//暂时用这个域名，以后改为https://api.nextevapp.com

    public static String getHost() {
        switch (SERVER_TYPE) {
            case TYPE_SERVER_OFFICIAL:
                return SERVER_OFFICIAL;
            case TYPE_SERVER_TEST:
                return SERVER_AWS_TEST;
            case TYPE_SERVER_DEV:
                return SERVER_AWS_DEV;
            case TYPE_SERVER_STAGING:
                return SERVER_AWS_STAGING;
            default:
                return SERVER_OFFICIAL;
        }
    }

    public static String getAppStoreHost() {
        switch (SERVER_TYPE) {
            case TYPE_SERVER_TEST:
                return NEXTEV_APP_STORE_HOST_TEST;
            default:
                return NEXTEV_APP_STORE_HOST;
        }
    }

    public static String getHostServer(@NonNull Context context) {
        return getHost();
    }
}
