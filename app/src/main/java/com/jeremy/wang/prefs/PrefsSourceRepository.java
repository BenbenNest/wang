package com.jeremy.wang.prefs;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * {@link SharedPreferences}源仓库
 *
 * @author jeremy
 */
public class PrefsSourceRepository {
    /**
     * 源仓库版本号。当数据信息有移除或者key变动时，需要修改版本号
     */
    private static final int NEW_VERSION = 1;

    public static AppPrefsSource getAppPrefsSource() {
        return AppPrefsSource.getInstance();
    }

    interface IPrefsSource {
        /**
         * 获取{@link SharedPreferences}对象
         *
         * @param ctx
         * @return
         */
        SharedPreferences getPrefs(Context ctx);
    }
}
