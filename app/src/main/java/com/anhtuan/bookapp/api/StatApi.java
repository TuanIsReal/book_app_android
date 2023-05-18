package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.response.IncomeAdminResponse;
import com.anhtuan.bookapp.response.IncomeMemberResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StatApi {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    StatApi statApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "stat/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(StatApi.class);

    @POST("incomeMember")
    Call<IncomeMemberResponse> incomeMember(@Query("userId") String userId,
                                            @Query("startDate") String startDate,
                                            @Query("endDate") String endDate);

    @POST("incomeAdmin")
    Call<IncomeAdminResponse> incomeAdmin(@Query("startDate") String startDate,
                                          @Query("endDate") String endDate);
}
