package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.request.AddBookRequest;
import com.anhtuan.bookapp.request.AddChapterRequest;
import com.anhtuan.bookapp.response.GetBannedWordResponse;
import com.anhtuan.bookapp.response.GetBookChapterListResponse;
import com.anhtuan.bookapp.response.GetBookResponse;
import com.anhtuan.bookapp.response.GetChapterContentResponse;
import com.anhtuan.bookapp.response.GetChapterResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface BookChapterApi extends BaseApi{

    BookChapterApi bookChapterApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "bookChapter/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
            .create(BookChapterApi.class);

    @POST("addChapter")
    Call<NoDataResponse> addChapter(@Body AddChapterRequest request);

    @GET("getChapterContent")
    Call<GetChapterContentResponse> getChapterContent(@Query("bookId") String bookId,
                                               @Query("chapterNumber") int chapterNumber);

    @GET("getChapterContentById")
    Call<GetChapterContentResponse> getChapterContentById(@Query("chapterId") String chapterId);

    @GET("getChapterInfo")
    Call<GetChapterResponse> getChapterInfo(@Query("chapterId") String chapterId);

    @GET("getBookChapterList")
    Call<GetBookChapterListResponse> getBookChapterList(@Query("bookId") String bookId);

    @POST("addBannedWord")
    Call<NoDataResponse> addBannedWord(@Query("word") String word);

    @POST("removeBannedWord")
    Call<NoDataResponse> removeBannedWord(@Query("word") String word);
}
