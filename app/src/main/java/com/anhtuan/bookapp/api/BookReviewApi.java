package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.request.AddBookReviewRequest;
import com.anhtuan.bookapp.response.GetBookReviewResponse;
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

public interface BookReviewApi {

    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    BookReviewApi bookReviewApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "bookReview/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(BookReviewApi.class);

    @POST("addBookReview")
    Call<NoDataResponse> addBookReview(@Body AddBookReviewRequest addBookReviewRequest);

    @GET("getBookReviews")
    Call<GetBookReviewResponse> getBookReviews(@Query("bookId") String bookId);

    @GET("getBookReview")
    Call<GetBookReviewResponse> getBookReview(@Query("bookId") String bookId,
                                              @Query("userId") String userId);
}
