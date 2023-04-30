package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.response.GetNotificationResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationApi {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    NotificationApi notificationApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "notification/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NotificationApi.class);

    @GET("getNotification")
    Call<GetNotificationResponse> getNotification(@Query("userId") String userId);

    @POST("clickNotification")
    Call<NoDataResponse> clickNotification(@Query("id") String id);
}
