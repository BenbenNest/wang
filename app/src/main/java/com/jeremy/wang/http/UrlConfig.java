package com.jeremy.wang.http;

/**
 * Created by pengyuan.xu on 17/2/23.
 */

public class UrlConfig {

    /**
     * api config info
     */
    public static class API {

        /**
         * app标识1001为Android,1002为iOS
         */
        public static final String API_ID = "10018";

        public static final String API = "api/";
        /**
         * 区域
         */
        public static final String REGION_CN = "cn";
        /**
         * 语言
         */
        public static final String LANG_ZH_CN = "zh-cn";
        /**
         * api os<li>android</li><li>ios</li>
         */
        public static final String OS_VALUE = "android";
        public static final String OS_KEY = "os";
        public static final String BRAND_KEY = "brand";
        public static final String MODEL_KEY = "model";
        public static final String EVENTS_KEY = "events";
        public static final String OS_VER_KEY = "os_ver";
        public static final String OS_TYPE_KEY = "os_type";
        public static final String APP_VER_KEY = "app_ver";
        public static final String NETWORK_KEY = "network";
        public static final String DEVICE_ID_KEY = "device_id";
        public static final String USER_ID_KEY = "user_id";
        public static final String OS_LANG_KEY = "os_lang";
        public static final String OS_TIME_ZONE_KEY = "os_timezone";
        public static final String CLIENT_TIME_STAMP_KEY = "client_timestamp";
    }
}
