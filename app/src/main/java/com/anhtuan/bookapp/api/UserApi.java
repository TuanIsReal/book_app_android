package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.request.GoogleLoginRequest;
import com.anhtuan.bookapp.request.AuthenVerifyCodeRequest;
import com.anhtuan.bookapp.request.RegisterRequest;
import com.anhtuan.bookapp.response.AuthenVerifyCodeResponse;
import com.anhtuan.bookapp.response.CheckLoggedResponse;
import com.anhtuan.bookapp.response.GetBalanceChangeResponse;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.response.GetUsernameResponse;
import com.anhtuan.bookapp.response.LoginResponse;
import com.anhtuan.bookapp.response.RegisterResponse;
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
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserApi {

    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    UserApi userApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "user/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserApi.class);

    @GET("login")
    Call<LoginResponse> login(@Query("email") String email,
                              @Query("password") String password,
                              @Query("ip") String ip);

    @POST("register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);


    @POST("google-login")
    Call<RegisterResponse> loginGoogle(@Body GoogleLoginRequest googleLoginRequest);

    @Multipart
    @POST("updateAvatarImage")
    Call<NoDataResponse> updateAvatarImage(@Part("userId") RequestBody bookName,
                                         @Part MultipartBody.Part image);

    @GET("checkExistUser")
    Call<NoDataResponse> checkExistUser(@Query("email") String email);

    @GET("getUserInfo")
    Call<GetUserInfoResponse> getUserInfo(@Query("userId") String userId);

    @GET("getUsername")
    Call<GetUsernameResponse> getUsername(@Query("userId") String userId);

    @PUT("logout")
    Call<NoDataResponse> logout(@Query("userId") String userId);

    @GET("checkUserLogged")
    Call<CheckLoggedResponse> checkUserLogged(@Query("ip") String ip);

    @GET("getAvatarImage")
    Call<ResponseBody> getAvatarImage(@Query("imageName") String imageName);

    @POST("updatePassword")
    Call<NoDataResponse> updatePassword(@Query("userId") String userId,
                                        @Query("password") String password,
                                        @Query("newPassword") String newPassword);

    @POST("updateName")
    Call<NoDataResponse> updateName(@Query("userId") String userId,
                                    @Query("password") String password,
                                    @Query("newName") String newName);

    @POST("forgotPassword")
    Call<NoDataResponse> forgotPassword(@Query("email") String email);

    @POST("authenVerifyCode")
    Call<AuthenVerifyCodeResponse> authenVerifyCode(@Body AuthenVerifyCodeRequest request);
    @POST("createNewPassword")
    Call<NoDataResponse> createNewPassword(@Query("userId") String userId,
                                        @Query("newPassword") String newPassword);
    @POST("getBalanceChange")
    Call<GetBalanceChangeResponse> getBalanceChange(@Query("userId") String userId);

    @POST("loginDevice")
    Call<NoDataResponse> loginDevice(@Query("userId") String userId,
                                     @Query("deviceToken") String deviceToken);
}
