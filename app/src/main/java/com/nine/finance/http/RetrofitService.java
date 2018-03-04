package com.nine.finance.http;

import com.nine.finance.app.AppGlobal;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pengyuan.xu on 17/2/23.
 */

public class RetrofitService {

    private Retrofit retrofit;
    private String host;
    private Interceptor interceptor;

    public RetrofitService() {
        host = ServiceHttpConfig.getHost();
        init();
    }

    public RetrofitService(String host) {
        this.host = host;
        init();
    }

    private void init() {
        interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request origin = chain.request();
                Request request;
                HttpUrl url = origin.url().newBuilder()
//                        .addQueryParameter("app_id", UrlConfig.API.API_ID)
//                        .addQueryParameter("app_ver", AppInfo.versionName)
//                        .addQueryParameter("region", UrlConfig.API.REGION_CN)
//                        .addQueryParameter("lang", UrlConfig.API.LANG_ZH_CN)
                        .build();

                Request.Builder requestBuilder;
                if (AppGlobal.getUserInfo() != null) {
                    requestBuilder = origin.newBuilder()
                            .addHeader("auth_token", AppGlobal.getUserInfo().getToken());
                } else {
                    requestBuilder = origin.newBuilder();
                }
//                Request.Builder requestBuilder = origin.newBuilder();
                request = requestBuilder.url(url).build();
                return chain.proceed(request);
            }
        };
    }

    public Retrofit getRetrofit() {
        OkHttpClient client;
        client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
