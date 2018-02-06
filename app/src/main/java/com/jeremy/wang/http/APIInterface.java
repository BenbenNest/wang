package com.jeremy.wang.http;

import com.jeremy.wang.model.BaseModel;
import com.jeremy.wang.model.UserLoginData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jeremy .
 */

public interface APIInterface {

    @FormUrlEncoded
    @POST("/api/1/account/user/login")
    Call<BaseModel<UserLoginData>> login(@FieldMap Map<String, String> bodyPara);










    /**
     * 检查APP更新信息
     *
     * @param app_ver_code
     * @param package_md5
     * @param brand
     * @param model
     * @param os
     * @param os_ver
     * @return
     */
//    @GET("/api/1/app/check_update")
//    Call<BaseModel<UpdateInfo>> getUpdateInfo(@Query("app_id") String app_id, @Query("app_ver_code") int app_ver_code, @Query("package_md5") String package_md5, @Query("brand") String brand, @Query("model") String model, @Query("os") String os, @Query("os_ver") String os_ver);



}
