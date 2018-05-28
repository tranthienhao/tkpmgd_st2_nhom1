package com.nhom1.englishspeaking.Retrofit2;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @FormUrlEncoded
    @POST("send_log.php")
    Call<String> sendLog(@Field("DeviceName") String DeviceName,
                         @Field("Status") String Status,
                         @Field("CreatedAt") String createAt);
}