package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.request.AddBookRequest;

import com.anhtuan.bookapp.request.GetBookFilterRequest;
import com.anhtuan.bookapp.request.UpdateBookRequest;
import com.anhtuan.bookapp.response.GetBookResponse;
import com.anhtuan.bookapp.response.GetRequestUploadBookResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.response.SearchBookResponse;
import com.anhtuan.bookapp.response.ViewBookResponse;
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

public interface BookApi {

    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    BookApi bookApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "book/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(BookApi.class);

    @POST("addBook")
    Call<NoDataResponse> addBook(@Body AddBookRequest addBookRequest);

    @POST("reactBookRequestUp")
    Call<NoDataResponse> reactBookRequestUp(@Query("bookId") String bookId,
                                            @Query("action") int action);

    @GET("getBookUp")
    Call<GetBookResponse> getBookByUserId(@Query("userId") String userId);

    @GET("getRequestUploadBook")
    Call<GetRequestUploadBookResponse> getRequestUploadBook(@Query("userId") String userId,
                                                            @Query("status") int status);

    @GET("searchBook")
    Call<SearchBookResponse> searchBook(@Query("text") String text);

    @GET("getAllRequestUploadBook")
    Call<GetRequestUploadBookResponse> getAllRequestUploadBook();

    @GET("getABook")
    Call<ViewBookResponse> getBookById(@Query("bookId") String bookId);

    @GET("getBookByAuthor")
    Call<SearchBookResponse> getBookByAuthor(@Query("author") String author,
                                             @Query("bookId") String bookId);

    @GET("getBookHome")
    Call<GetBookResponse> getBookHome(@Query("typeFilter") int typeFilter,
                                      @Query("limit") int limit);

    @POST("getBookFilter")
    Call<GetBookResponse> getBookFilter(@Body GetBookFilterRequest request);

    @POST("updateBookInfo")
    Call<NoDataResponse> updateBookInfo(@Body UpdateBookRequest updateBookRequest);
}
