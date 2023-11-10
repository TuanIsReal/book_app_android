package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.response.LoginResponse;
import com.anhtuan.bookapp.response.RefreshTokenResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UnAuthApi {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    UnAuthApi unAuthApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UnAuthApi.class);

    @GET("user/refreshToken")
    Call<RefreshTokenResponse> refreshToken(@Query("refreshToken") String refreshToken);

    @GET("user/reLogin")
    Call<LoginResponse> reLogin(@Query("email") String email,
                                @Query("password") String password);
}
