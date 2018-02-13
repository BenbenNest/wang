package com.nine.finance.utils;

import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * {@code Gson}工具类
 *
 */
public class GsonCore {
    /**
     * 将{@code src}序列化为{@code json}字符串
     *
     * @param src 待序列化对象
     * @return {@code json}字符串
     */
    public static String toJson(Object src) {
        Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create();
        return gson.toJson(src);
    }

    /**
     * 将{@code json}字符串反序列化为{@code T}对应的对象
     *
     * @param json     {@code json}字符串
     * @param classOfT 反序列化的目标{@code class}
     * @param <T>      反序列化的目标对象
     * @return 反序列化的目标对象
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create();
        return gson.fromJson(TextUtils.isEmpty(json) ? json : json.trim(), classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        if (typeOfT == null) {
            throw new IllegalStateException("Gson解析所需的 Model TypeToken 不可为空");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();
        return gson.fromJson(TextUtils.isEmpty(json) ? json : json.trim(), typeOfT);
    }
}
