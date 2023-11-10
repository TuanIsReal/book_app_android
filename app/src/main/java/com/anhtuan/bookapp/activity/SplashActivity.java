package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.AuthApi.authApi;
import static com.anhtuan.bookapp.api.BookChapterApi.bookChapterApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.api.UserApi;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.ApiAddress;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.domain.BannedWord;
import com.anhtuan.bookapp.response.CheckLoggedResponse;
import com.anhtuan.bookapp.response.GetBannedWordResponse;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.response.PingResponse;
import com.anhtuan.bookapp.retrofit.RetrofitService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

//    private FirebaseAuth firebaseAuth;
    private static final int MY_REQUEST_CODE = 20000;
    int bannedVersion;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_splash);
//        requestPermission();
        AccountManager.getInstance().saveAccount("gvbchatbot@gmail.com", null);
        TokenManager.getInstance().saveToken("12jk54b", "feefef");

        authApi.ping().enqueue(new RetrofitCallBack<PingResponse>() {
            @Override
            public void onSuccess(PingResponse response) {
                if (response != null && response.getCode() == 100){
                    Log.d("PING", "PONG");
                }else {
                    Log.d("PING", "Fail");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("PING ERROR", errorMessage);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                checkLogged();
                sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String userId = sharedPreferences.getString("userId","");
                bannedVersion = sharedPreferences.getInt("bannedVersion",1);
                getBannedWord();
                if (userId.isBlank()){
                    finish();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                else {
                    autoLogin(userId);
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            }
        }, 1000);
    }

    private void autoLogin(String userId){

        userApi.getUserInfo(userId).enqueue(new Callback<GetUserInfoResponse>() {
            @Override
            public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                if (response.body().getCode() == 106){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    GetUserInfoResponse userInfoResponse = response.body();
                    int role = userInfoResponse.getData().getRole();
                    sendDeviceToken(userId, role);
                }
            }

            @Override
            public void onFailure(Call<GetUserInfoResponse> call, Throwable t) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void sendDeviceToken(String userId, int role){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                String token = task.getResult();
                userApi.loginDevice(userId, token).enqueue(new Callback<NoDataResponse>() {
                    @Override
                    public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                        if (response.body() != null){
                            NoDataResponse responseBody = response.body();
                            if (responseBody.getCode() == 106){

                            } else if (responseBody.getCode() == 100) {
                                if (role == 2){
                                    startActivity(new Intent(SplashActivity.this, DashboardAdminActivity.class));
                                    finish();
                                }
                                else {
                                    startActivity(new Intent(SplashActivity.this, DashboardUserActivity.class));
                                    finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NoDataResponse> call, Throwable t) {
                        if (role == 2){
                            startActivity(new Intent(SplashActivity.this, DashboardAdminActivity.class));
                            finish();
                        }
                        else {
                            startActivity(new Intent(SplashActivity.this, DashboardUserActivity.class));
                            finish();
                        }
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
                    if (user.getRole() == 1){
                        startActivity(new Intent(SplashActivity.this, DashboardUserActivity.class));
                        finish();
                    }
                    if (user.getRole() == 2){
                        startActivity(new Intent(SplashActivity.this, DashboardAdminActivity.class));
                        finish();
                    }
                }
                else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CheckLoggedResponse> call, Throwable t) {

            }
        });
    }

    private void requestPermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            String[] permission;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permission = new String[]{Manifest.permission.POST_NOTIFICATIONS};
                requestPermissions(permission, MY_REQUEST_CODE);
            }
        }
    }

    private void getBannedWord() {
        bookChapterApi.getBannedWord(bannedVersion).enqueue(new Callback<GetBannedWordResponse>() {
            @Override
            public void onResponse(Call<GetBannedWordResponse> call, Response<GetBannedWordResponse> response) {
                if (response.body() == null || response.body().getCode() == 98){
                    return;
                }
                BannedWord bannedWord = response.body().getData();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("bannedVersion", bannedWord.getVersion());
                editor.apply();
                File file = new File(getFilesDir(), "banned_words.txt");

                try (FileWriter writer = new FileWriter(file)){
                    for (String word : bannedWord.getWords()) {
                        writer.write(word + "\n");
                    }
                } catch (IOException e){

                }
            }

            @Override
            public void onFailure(Call<GetBannedWordResponse> call, Throwable t) {
            }
        });
    }
}