package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.response.NoDataResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface STFApi extends BaseApi{

    STFApi stfApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "stf/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
            .create(STFApi.class);

    @Multipart
    @POST("updateBookImage")
    Call<NoDataResponse> updateBookImage(@Part("bookName") RequestBody bookName,
                                         @Part MultipartBody.Part image);

    @Multipart
    @POST("updateAvatarImage")
    Call<NoDataResponse> updateAvatarImage(@Part MultipartBody.Part image);
}
