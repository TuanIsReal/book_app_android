package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DeviceApi {

    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    DeviceApi deviceApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "device/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(DeviceApi.class);

    @POST("loginDevice")
    Call<NoDataResponse> loginDevice(@Query("userId") String userId,
                                     @Query("deviceToken") String deviceToken);
}
