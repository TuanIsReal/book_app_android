package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.request.AddBookRequest;
import com.anhtuan.bookapp.request.AddCommentRequest;
import com.anhtuan.bookapp.response.GetBookResponse;
import com.anhtuan.bookapp.response.GetCommentListResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.response.SearchBookResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommentApi extends BaseApi{

    CommentApi commentApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "comment/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
            .create(CommentApi.class);

    @POST("addComment")
    Call<NoDataResponse> addComment(@Body AddCommentRequest request);

    @GET("getCommentList")
    Call<GetCommentListResponse> getCommentList(@Query("bookId") String bookId);
}
