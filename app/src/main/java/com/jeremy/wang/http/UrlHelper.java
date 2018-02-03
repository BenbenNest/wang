package com.jeremy.wang.http;

import com.jeremy.wang.constant.Constant;
import com.jeremy.wang.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * Created by jeremy
 */
class UrlHelper {
    public static Map<String, String> mParam = new HashMap<>();

    static {
        mParam.put("app_id", UrlConfig.API.API_ID);
        mParam.put("app_ver", AppInfo.versionName);
        mParam.put("region", UrlConfig.API.REGION_CN);
        mParam.put("lang", UrlConfig.API.LANG_ZH_CN);
    }

    /**
     * 无参URL para生成
     *
     * @return
     */
    public static Map<String, String> createGETUrlParams() {
        SortedMap<String, String> cal = new TreeMap<>();
        cal.putAll(mParam);

        SortedMap<String, String> result = new TreeMap<>();
        result.put("sig", SignUtils.createSign(Constant.APPSERICT, null, cal));

        return result;
    }

    /**
     * 带url参数的 url para生成
     *
     * @param para
     * @return
     */
    public static Map<String, String> createGETUrlParams(Map<String, String> para) {
        SortedMap<String, String> cal = new TreeMap<>();
        cal.putAll(para);
        cal.putAll(mParam);
        String sig = SignUtils.createSign(Constant.APPSERICT, null, cal);

        SortedMap<String, String> result = new TreeMap<>();
        result.putAll(para);
        result.put("sig", sig);

        return result;
    }

    /**
     * 无body参数的 url para生成
     *
     * @return
     */
    public static Map<String, String> createPOSTUrlParams() {
        Map<String, String> result = new HashMap<>();
        result.putAll(mParam);

        SortedMap<String, String> cal = new TreeMap<>();
        cal.putAll(result);
        String sig = SignUtils.createSign(Constant.APPSERICT, null, cal);

        result.put("sig", sig);
        return result;
    }

    /**
     * 带body参数的 url para生成
     *
     * @param bodyPara
     * @return
     */
    public static Map<String, String> createPOSTUrlParams(Map<String, String> bodyPara) {
        SortedMap<String, String> cal = new TreeMap<>();
        cal.putAll(bodyPara);
        cal.putAll(mParam);
        String sig = SignUtils.createSign(Constant.APPSERICT, null, cal);

        Map<String, String> result = new HashMap<>();
        result.put("sig", sig);
        return result;
    }

    /**
     * 带body参数和url参数的url para生成
     *
     * @param bodyPara
     * @param urlPara
     * @return
     */
    public static Map<String, String> createPOSTUrlParams(Map<String, String> bodyPara, Map<String, String> urlPara) {
        Map<String, String> result = new HashMap<>();
        result.putAll(mParam);
        result.putAll(urlPara);

        SortedMap<String, String> cal = new TreeMap<>();
        cal.putAll(bodyPara);
        cal.putAll(result);
        String sig = SignUtils.createSign(Constant.APPSERICT, null, cal);

        result.put("sig", sig);
        return result;
    }
}
