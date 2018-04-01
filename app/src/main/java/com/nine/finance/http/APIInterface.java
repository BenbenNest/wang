package com.nine.finance.http;

import com.nine.finance.model.BankInfo;
import com.nine.finance.model.BankIntroContract;
import com.nine.finance.model.BaseModel;
import com.nine.finance.model.BranchInfo;
import com.nine.finance.model.HomeInfo;
import com.nine.finance.model.ImageInfo;
import com.nine.finance.model.UserInfo;
import com.nine.finance.model.VerifyCodeModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by jeremy .
 */

public interface APIInterface {
    /**
     * 1. 图片上传 OK
     * 2. 4要素认证 OK
     * 3. 文本框验证 OK
     * 4. 活体和大头贴界面
     * 5。验证码60S  OK
     * 6。auth_token  OK
     */

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/account/rest/user/login")
    Call<BaseModel<UserInfo>> login(@Body RequestBody body);

    @GET("/account/rest/user/logout")
    Call<BaseModel<Boolean>> logOut();

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/account/rest/user/creating")
    Call<BaseModel<UserInfo>> register(@Body RequestBody body);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/account/rest/user/updating")
    Call<BaseModel<UserInfo>> updateUserinfo(@Body RequestBody body);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @GET("/account/rest/bankcard/findByUserId")
    Call<BaseModel<List<BankInfo>>> getApplyBankList(@Query("userId") String userId);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @GET("/account/rest/bank/findByBankList")
    Call<BaseModel<List<BankInfo>>> getBankList();

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @GET("/account/rest/bank/getgAreementByBankId")
    Call<BaseModel<BankIntroContract>> getBankIntroOrContract(@Query("bankId") String bankId);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @GET("/account/rest/bank/findBankBranchesInfoAll")
    Call<BaseModel<List<BranchInfo>>> getBranchList(@Query("bankId") String bankId);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/account/rest/bankcard/creating")
    Call<BaseModel<String>> applyCard(@Body RequestBody body);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @GET("/account/rest/sms/sendMessage")
    Call<BaseModel<VerifyCodeModel>> getVerifyCode(@Query("phone") String phone);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @GET("/account/rest/sms/smsAuth")
    Call<BaseModel<String>> verifyCode(@Query("phone") String phone, @Query("code") String code);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @GET("/account/rest/region/getOrigin")
    Call<BaseModel<List<HomeInfo>>> getHome();

    @Multipart
//    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/account/rest/bankcard/uploadFile")
    Call<BaseModel<ImageInfo>> uploadFile(@Part MultipartBody.Part body);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/account/rest/bankcard/elementsRecognition")
    Call<BaseModel<VerifyCodeModel>> elementsRecognition(@Body RequestBody body);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @GET("/account/rest/user/forgetPassword_")
    Call<BaseModel<String>> forgetPassword(@Query("phone") String phone, @Query("code") String code, @Query("type") String type, @Query("password") String password);


}
