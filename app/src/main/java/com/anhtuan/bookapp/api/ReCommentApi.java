package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.request.AddCommentRequest;
import com.anhtuan.bookapp.request.AddReCommentRequest;
import com.anhtuan.bookapp.response.GetCommentListResponse;
import com.anhtuan.bookapp.response.GetReCommentListResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReCommentApi extends BaseApi{

    ReCommentApi reCommentApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "reComment/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
            .create(ReCommentApi.class);

    @POST("addReComment")
    Call<NoDataResponse> addReComment(@Body AddReCommentRequest request);

    @GET("getReCommentList")
    Call<GetReCommentListResponse> getReCommentList(@Query("parentCommentId") String parentCommentId);
}
