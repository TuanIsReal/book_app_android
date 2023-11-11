package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.common.TokenManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public interface BaseApi {
    public Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    public OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
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
}
