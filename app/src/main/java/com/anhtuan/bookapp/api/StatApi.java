package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.response.IncomeAdminResponse;
import com.anhtuan.bookapp.response.IncomeMemberResponse;
import com.anhtuan.bookapp.response.RankingBookResponse;
import com.anhtuan.bookapp.response.RankingUserResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StatApi extends BaseApi{

    StatApi statApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "stat/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
            .create(StatApi.class);

    @POST("incomeMember")
    Call<IncomeMemberResponse> incomeMember(@Query("startDate") String startDate,
                                            @Query("endDate") String endDate);

    @POST("rankingUser")
    Call<RankingUserResponse> getRankingUser(@Query("typeRanking") int typeRanking);

    @POST("rankingBook")
    Call<RankingBookResponse> getRankingBook();

    @POST("incomeAdmin")
    Call<IncomeAdminResponse> incomeAdmin(@Query("startDate") String startDate,
                                          @Query("endDate") String endDate);
}
