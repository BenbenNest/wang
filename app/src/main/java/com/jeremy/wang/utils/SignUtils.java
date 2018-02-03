package com.jeremy.wang.utils;

import android.text.TextUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * Created by jeremy
 */

public class SignUtils {
    public static String createSign(String appSecret, String characterEncoding, SortedMap<String,String> parameters){
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, String>> es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator<Map.Entry<String, String>> it = es.iterator();
        while(it.hasNext()) {
            Map.Entry<String, String>  entry = it.next();
            String k = entry.getKey();
            String v = entry.getValue();
            if (!TextUtils.isEmpty(k) && !TextUtils.isEmpty(v)) {
                sb.append(k);
                sb.append('=');
                sb.append(v);
                sb.append('&');
            }
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(appSecret);
        return MD5Util.MD5Encode(sb.toString(), characterEncoding);
    }
}
