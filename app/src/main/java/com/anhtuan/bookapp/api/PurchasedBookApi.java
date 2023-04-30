package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.response.BaseResponse;
import com.anhtuan.bookapp.response.CheckPurchasedBookResponse;
import com.anhtuan.bookapp.response.GetBookResponse;
import com.anhtuan.bookapp.response.GetPurchasedBookResponse;
import com.anhtuan.bookapp.response.GetUserBookLibraryResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PurchasedBookApi {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    PurchasedBookApi purchasedBookApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "purchasedBook/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PurchasedBookApi.class);

    @GET("getUserBookLibrary")
    Call<GetUserBookLibraryResponse> getUserBookLibrary(@Query("userId") String userId);

    @GET("checkPurchasedBook")
    Call<CheckPurchasedBookResponse> checkPurchasedBook(@Query("bookId") String bookId,
                                                        @Query("userId") String userId);

    @POST("buyBook")
    Call<BaseResponse> buyBook(@Query("userId") String userId,
                               @Query("bookId") String bookId);

    @GET("getPurchasedBook")
    Call<GetPurchasedBookResponse> getPurchasedBook(@Query("bookId") String bookId,
                                                    @Query("userId") String userId);
}
