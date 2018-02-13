package com.nine.finance.http;

import com.nine.finance.model.BaseModel;
import com.nine.finance.model.UserLoginData;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by jeremy .
 */

public interface APIInterface {


//    ip:8080/web/user/login

    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @FormUrlEncoded
    @POST("/account/rest/user/login")
    Call<BaseModel<UserLoginData>> login(@Body RequestBody body);

    @GET("/account/user/logout")
    Call<BaseModel<String>> logOut();

    @FormUrlEncoded
    @POST("/web/user/register")
    Call<BaseModel<UserLoginData>> register(@FieldMap Map<String, String> params);
//    Integer id；(可以为null)
//    String name;（必须有）
//    String password;(传输前sha1加密)
//    String headSculpture;
//    String phoneNumber;
//    String address;
//    String idNumber;

    @FormUrlEncoded
    @POST("/web/user/update")
    Call<BaseModel<UserLoginData>> updateUserInfo(@FieldMap Map<String, String> params);
//    Integer id；(可以为null)
//    String name;（必须有）
//    String password;(传输前sha1加密)
//    String headSculpture;
//    String phoneNumber;
//    String address;
//    String idNumber;


//5、GET:user/productGoodMap/countByGood 某商品当前共关联了多少理财产品
//    Integer goodId;
//
//6、GET:user/productGoodMap/listByGood 某商品关联理财产品分页展示
//    Integer goodId;
//    Integer start
//    Integer pageSize
//
//7、GET:user/productGoodMap/countByProduct 某理财产品当前共关联了多少理财产品商品
//    Integer productId;
//
//8、GET:user/productGoodMap/listByGood 理财产品关联的商品分页展示
//    Integer productId;
//    Integer start
//    Integer pageSize
//
//9、GET：user/goods/count
//    Integer sellerId
//
//10、GET：user/goods/list
//    Integer sellerId;
//    Integer start
//    Integer pageSize
//
//11、GET：user/outlet/count
//    Integer companyId
//
//12、GET：user/outlet/list
//    Integer companyId;
//    Integer start
//    Integer pageSize
//
//11、GET：user/company/list
//
//12、GET：user/product/count
//    Integer companyId;
//
//13、GET：user/product/list
//    Integer companyId;
//    Integer start
//    Integer pageSize
//
//14、GET: user/company
//    Integer id;
//
//15、GET: user/outlet
//    Integer id;
//
//16、GET: user/product
//    Integer id;
//
//17、GET: user/goods
//    Integer id;


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
