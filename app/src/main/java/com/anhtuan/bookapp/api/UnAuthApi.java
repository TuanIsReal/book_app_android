package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.request.AuthenVerifyCodeRequest;
import com.anhtuan.bookapp.request.GoogleLoginRequest;
import com.anhtuan.bookapp.request.RegisterRequest;
import com.anhtuan.bookapp.response.AuthenVerifyCodeResponse;
import com.anhtuan.bookapp.response.GetBannedWordResponse;
import com.anhtuan.bookapp.response.ImageResponse;
import com.anhtuan.bookapp.response.LoginResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.response.PingResponse;
import com.anhtuan.bookapp.response.RefreshTokenResponse;
import com.anhtuan.bookapp.response.RegisterResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
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

public interface UnAuthApi {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    UnAuthApi unAuthApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UnAuthApi.class);

    @GET("user/refreshToken")
    Call<RefreshTokenResponse> refreshToken(@Query("refreshToken") String refreshToken);

    @GET("user/reLogin")
    Call<LoginResponse> reLogin(@Query("email") String email,
                                @Query("password") String password);

    @GET("user/login")
    Call<LoginResponse> login(@Query("email") String email,
                              @Query("password") String password,
                              @Query("ip") String ip);

    @POST("user/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("user/google-login")
    Call<LoginResponse> loginGoogle(@Body GoogleLoginRequest googleLoginRequest);

    @GET("user/checkExistUser")
    Call<NoDataResponse> checkExistUser(@Query("email") String email);


    @POST("user/forgotPassword")
    Call<NoDataResponse> forgotPassword(@Query("email") String email);

    @POST("user/authenVerifyCode")
    Call<AuthenVerifyCodeResponse> authenVerifyCode(@Body AuthenVerifyCodeRequest request);

    @POST("user/createNewPassword")
    Call<NoDataResponse> createNewPassword(@Query("userId") String userId,
                                           @Query("newPassword") String newPassword);

    @GET("stf/getBookImage")
    Call<ImageResponse> getBookImage(@Query("imageName") String imageName);

    @GET("stf/getAvatar")
    Call<ImageResponse> getAvatar(@Query("imageName") String imageName);

    @GET("stf/getThumbnail")
    Call<ImageResponse> getThumbnail(@Query("thumbnailName") String thumbnailName);

    @GET("bookChapter/getBannedWord")
    Call<GetBannedWordResponse> getBannedWord(@Query("version") int version);

    @GET("user/ping")
    Call<PingResponse> ping();

}
