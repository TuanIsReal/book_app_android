package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.request.AddBookRequest;
import com.anhtuan.bookapp.response.GetRequestUploadBookResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.response.NumberResponse;
import com.anhtuan.bookapp.response.SearchBookResponse;
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

public interface BookRequestUpApi {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    BookRequestUpApi bookRequestUpApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "bookRequestUp/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(BookRequestUpApi.class);

//    @POST("addBookRequestUp")
//    Call<NoDataResponse> addBook(@Body AddBookRequest addBookRequest);
//
//    @Multipart
//    @POST("updateBookRequestUpImage")
//    Call<NoDataResponse> updateBookRequestUpImage(@Part("bookName") RequestBody bookName,
//                                         @Part MultipartBody.Part image);
//
//    @GET("getRequestUploadBook")
//    Call<GetRequestUploadBookResponse> getRequestUploadBook(@Query("userId") String userId,
//                                                            @Query("status") int status);
//
//    @GET("getAllRequestUploadBook")
//    Call<GetRequestUploadBookResponse> getAllRequestUploadBook();
//
//    @GET("getBookRequestUpImage")
//    Call<ResponseBody> getBookRequestUpImage(@Query("imageName") String imageName);
//
//    @POST("reactBookRequestUp")
//    Call<NoDataResponse> reactBookRequestUp(@Query("bookId") String bookId,
//                                            @Query("action") int action);
//
//    @GET("getQuantityPurchased")
//    Call<NumberResponse> getQuantityPurchased(@Query("bookId") String bookId,
//                                              @Query("userId") String userId);
}
