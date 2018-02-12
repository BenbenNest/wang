package com.nine.finance.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;

/**
 * {@link SharedPreferences}帮助类
 *
 * @author wilson.wu
 */
public class PrefsUtils {

    /**
     * 获取@{@code name}对应的{@linkplain SharedPreferences preferences}
     *
     * @param ctx
     * @param name {@linkplain SharedPreferences preferences}的名字
     * @return
     */
    public static SharedPreferences asPrefs(@NonNull Context ctx, String name) {
        return ctx.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * 获取{@code name}对应的@{@linkplain SharedPreferences preferences}
     * {@link Editor}
     *
     * @param ctx
     * @param name {@linkplain SharedPreferences preferences}的名字
     * @return
     */
    public static Editor asEditor(Context ctx, String name) {
        return asPrefs(ctx, name).edit();
    }

}
