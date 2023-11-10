package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.response.GetBannedWordResponse;
import com.anhtuan.bookapp.response.PingResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthApi {

    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + TokenManager.getInstance().getToken())
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

    AuthApi authApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
            .create(AuthApi.class);

    @GET("bookChapter/getBannedWord")
    Call<GetBannedWordResponse> getBannedWord(@Query("version") int version);

    @GET("user/ping")
    Call<PingResponse> ping();
}
