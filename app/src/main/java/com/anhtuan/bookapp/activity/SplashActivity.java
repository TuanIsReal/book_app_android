package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.DeviceApi.deviceApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.api.UserApi;
import com.anhtuan.bookapp.common.ApiAddress;
import com.anhtuan.bookapp.response.CheckLoggedResponse;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.retrofit.RetrofitService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

//    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                checkLogged();
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String userId = sharedPreferences.getString("userId","");
                if (userId.isBlank()){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
                else {
                    autoLogin(userId);
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            }
        }, 2000);
    }

    private void autoLogin(String userId){

        userApi.getUserInfo(userId).enqueue(new Callback<GetUserInfoResponse>() {
            @Override
            public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                GetUserInfoResponse userInfoResponse = response.body();
                String role = userInfoResponse.getData().getRole();
                sendDeviceToken(userId, role);
            }

            @Override
            public void onFailure(Call<GetUserInfoResponse> call, Throwable t) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void sendDeviceToken(String userId, String role){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()){
                    Log.d("Token LOG--", "No Token");
                }

                String token = task.getResult();
                deviceApi.loginDevice(userId, token).enqueue(new Callback<NoDataResponse>() {
                    @Override
                    public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                        if (response.body() != null){
                            NoDataResponse responseBody = response.body();
                            if (responseBody.getCode() == 106){

                            } else if (responseBody.getCode() == 100) {
                                if (role.equals("admin")){
                                    startActivity(new Intent(SplashActivity.this, DashboardAdminActivity.class));
                                }
                                else {
                                    startActivity(new Intent(SplashActivity.this, DashboardUserActivity.class));
                                }
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NoDataResponse> call, Throwable t) {
                        if (role.equals("admin")){
                            startActivity(new Intent(SplashActivity.this, DashboardAdminActivity.class));
                        }
                        else {
                            startActivity(new Intent(SplashActivity.this, DashboardUserActivity.class));
                        }
                        finish();
                    }
                });
            }
        });
    }

    private void checkLogged(){
        String ip = new ApiAddress().getIPAddress();

        userApi.checkUserLogged(ip).enqueue(new Callback<CheckLoggedResponse>() {
            @Override
            public void onResponse(Call<CheckLoggedResponse> call, Response<CheckLoggedResponse> response) {
                CheckLoggedResponse checkResponse = response.body();
                if (checkResponse.getCode() == 100){
                    User user = checkResponse.getData();
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userId", user.getId());
                    editor.apply();
                    if (user.getRole().equals("member")){
                        startActivity(new Intent(SplashActivity.this, DashboardUserActivity.class));
                    }
                    if (user.getRole().equals("admin")){
                        startActivity(new Intent(SplashActivity.this, DashboardAdminActivity.class));
                    }
                }
                else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<CheckLoggedResponse> call, Throwable t) {

            }
        });
    }
}