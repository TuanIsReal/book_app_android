package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;
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
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.ApiAddress;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.domain.BannedWord;
import com.anhtuan.bookapp.response.CheckLoggedResponse;
import com.anhtuan.bookapp.response.CheckUserInfoResponse;
import com.anhtuan.bookapp.response.GetBannedWordResponse;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.response.PingResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 20000;
    int bannedVersion;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("appInfo", Context.MODE_PRIVATE);
//        requestPermission();

        unAuthApi.ping().enqueue(new RetrofitCallBack<PingResponse>() {
            @Override
            public void onSuccess(PingResponse response) {
                openApp();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(SplashActivity.this, "Server hệ thống đang bảo trì!!! Xin bạn thông cảm và quay lại app sau", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void openApp(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bannedVersion = sharedPreferences.getInt("bannedVersion",1);
                getBannedWord();
                String email = AccountManager.getInstance().getEmail();
                if (email == null || email.isBlank() || email.equals("")){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
                else {
                    autoLogin();
                }
            }
        }, 1000);
    }

    private void autoLogin(){

        userApi.checkUserInfo().enqueue(new RetrofitCallBack<CheckUserInfoResponse>() {
            @Override
            public void onSuccess(CheckUserInfoResponse response) {
                if (response.getCode() == 106){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    int role = response.getData().getRole();
                    sendDeviceToken(role);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                TokenManager.getInstance().deleteToken();
                AccountManager.getInstance().logoutAccount();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void sendDeviceToken(int role){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                String token = task.getResult();

                userApi.loginDevice(token).enqueue(new RetrofitCallBack<NoDataResponse>() {
                    @Override
                    public void onSuccess(NoDataResponse response) {
                        if (response.getCode() == 106){

                        } else if (response.getCode() == 100) {
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

                    @Override
                    public void onFailure(String errorMessage) {
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
        unAuthApi.getBannedWord(bannedVersion).enqueue(new Callback<GetBannedWordResponse>() {
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