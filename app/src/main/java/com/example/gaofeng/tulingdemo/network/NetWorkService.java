package com.example.gaofeng.tulingdemo.network;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NetWorkService {

    //文本类请求
//    @POST("api")
//    Observable<String> getTextInfo(@Query("key") String key,
//                                     @Query("info") String info);
    //文本类请求
    @POST("api")
    Call<ResponseBody> getTextInfo(@Query("key") String key,
                                   @Query("info") String info);

    //需要地址类请求1
    @POST("api")
    Call<ResponseBody> getUrlInfo1(@Query("key") String key,
                                   @Query("info") String info,
                                   @Query("loc") String loc,
                                   @Query("userid") String userid);

    @POST("api")
    Call<ResponseBody> getUrlInfo2(@Query("key") String key,
                                   @Query("info") String info,
                                   @Query("loc") String loc,
                                   @Query("lon") String lon,
                                   @Query("lat") String lat,
                                   @Query("userid") String userid);
}