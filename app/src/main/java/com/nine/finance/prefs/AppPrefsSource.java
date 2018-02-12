package com.nine.finance.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.nine.finance.utils.GsonCore;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by jeremy
 */

public class AppPrefsSource implements PrefsSourceRepository.IPrefsSource {
    private static volatile AppPrefsSource INSTANCE;

    private AppPrefsSource() {
    }

    public static AppPrefsSource getInstance() {
        if (INSTANCE == null) {
            synchronized (AppPrefsSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppPrefsSource();
                }
            }
        }

        return INSTANCE;
    }

    @Override
    public SharedPreferences getPrefs(Context ctx) {
        return PrefsUtils.asPrefs(ctx.getApplicationContext(), "FellowApp");
    }

    /**
     * {@link SharedPreferences}的{@code key}
     *
     * @author wilson.wu
     */
    public static class Key {
        /**
         * 域名类型
         */
        static final String OFR_DOMAIN = "NextEV_domain";

        /**
         * 用户头像
         */
        public static final String USER_HEAD_IMAGE = "user_head_image_url";

        /**
         * 用户修改头像
         */
        public static final String HEAD_MODIFY = "head_modify";
        public static final String NAME_MODIFY = "name_modify";

        public static final String MESSAGE = "message";
    }

    /**
     * 保存用户数据map
     *
     * @param context
     * @param map
     */
    public void setUserInfo(@NonNull Context context, Map<String, String> map) {
        String mapString = GsonCore.toJson(map);
        getPrefs(context)
                .edit()
                .putString(Key.MESSAGE, mapString)
                .commit();
    }

    /**
     * 获取用户数据map
     *
     * @param context
     * @return
     */
    @Nullable
    public HashMap<String, String> getUserInfo(@NonNull Context context) {
        String mapJson = getPrefs(context).getString(Key.MESSAGE, null);
        if (TextUtils.isEmpty(mapJson)) {
            return null;
        }

        return GsonCore.fromJson(mapJson, new TypeToken<HashMap<String, String>>() {
        }.getType());
    }

}
