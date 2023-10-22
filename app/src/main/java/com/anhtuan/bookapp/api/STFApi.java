package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.response.ImageResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface STFApi {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    STFApi stfApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "stf/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(STFApi.class);

    @Multipart
    @POST("updateBookImage")
    Call<NoDataResponse> updateBookImage(@Part("bookName") RequestBody bookName,
                                         @Part MultipartBody.Part image);

    @Multipart
    @POST("updateAvatarImage")
    Call<NoDataResponse> updateAvatarImage(@Part("userId") RequestBody userId,
                                           @Part MultipartBody.Part image);

    @GET("getBookImage")
    Call<ImageResponse> getBookImage(@Query("imageName") String imageName);

    @GET("getAvatar")
    Call<ImageResponse> getAvatar(@Query("imageName") String imageName);

    @GET("getThumbnail")
    Call<ImageResponse> getThumbnail(@Query("thumbnailName") String thumbnailName);
}
