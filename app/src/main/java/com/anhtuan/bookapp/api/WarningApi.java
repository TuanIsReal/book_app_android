package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.response.GetWarningListResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WarningApi {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    WarningApi warningApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "warning/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WarningApi.class);

    @GET("getWarningList")
    Call<GetWarningListResponse> getWarningList();

    @POST("reactWarning")
    Call<NoDataResponse> getBookImage(@Query("chapter") String chapter,
                                      @Query("react") int react);
}
