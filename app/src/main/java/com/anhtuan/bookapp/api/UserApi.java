package com.anhtuan.bookapp.api;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.request.GoogleLoginRequest;
import com.anhtuan.bookapp.request.AuthenVerifyCodeRequest;
import com.anhtuan.bookapp.request.RegisterRequest;
import com.anhtuan.bookapp.response.AuthenVerifyCodeResponse;
import com.anhtuan.bookapp.response.CheckLoggedResponse;
import com.anhtuan.bookapp.response.CheckUserInfoResponse;
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

public interface UserApi extends BaseApi{

    UserApi userApi = new Retrofit.Builder()
            .baseUrl(Constant.IP_SERVER + "user/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
            .create(UserApi.class);

    @GET("getUserInfo")
    Call<GetUserInfoResponse> getUserInfo(@Query("userId") String userId);

    @GET("checkUserInfo")
    Call<CheckUserInfoResponse> checkUserInfo();

    @GET("getUsername")
    Call<GetUsernameResponse> getUsername(@Query("userId") String userId);

    @PUT("logout")
    Call<NoDataResponse> logout();

    @GET("checkUserLogged")
    Call<CheckLoggedResponse> checkUserLogged(@Query("ip") String ip);

    @POST("updatePassword")
    Call<NoDataResponse> updatePassword(@Query("password") String password,
                                        @Query("newPassword") String newPassword);

    @POST("updateName")
    Call<NoDataResponse> updateName(@Query("password") String password,
                                    @Query("newName") String newName);

    @POST("getBalanceChange")
    Call<GetBalanceChangeResponse> getBalanceChange();

    @POST("loginDevice")
    Call<NoDataResponse> loginDevice(@Query("deviceToken") String deviceToken);
}
