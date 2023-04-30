package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.DeviceApi.deviceApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.api.UserApi;
import com.anhtuan.bookapp.common.ApiAddress;
import com.anhtuan.bookapp.databinding.ActivityLoginBinding;
import com.anhtuan.bookapp.response.LoginData;
import com.anhtuan.bookapp.response.LoginResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.retrofit.RetrofitService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Xin chờ");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private String email, password;

    private void validateData(){
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();

        if (email.isBlank() || password.isBlank()){
            Toast.makeText(this, "Chưa nhập email/password", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email chưa đúng định dạng", Toast.LENGTH_SHORT).show();
        } else {
            loginUser();
        }
    }

    private void loginUser() {
        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.show();

        userApi.login(email, password, new ApiAddress().getIPAddress()).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.setMessage("Đang kiểm tra...");
                LoginResponse loginResponse = response.body();
                if (loginResponse.getCode() == 102) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu chưa đúng", Toast.LENGTH_SHORT).show();
                } else if (loginResponse.getCode() == 100) {
                    LoginData loginData = (LoginData) loginResponse.getData();
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userId", loginData.getUserId());
                    editor.putString("userRole", loginData.getRole());
                    editor.putString("theme", "light");
                    editor.apply();
                    String role = loginData.getRole();
                    sendDeviceToken(loginData.getUserId(), role);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, ""+ t, Toast.LENGTH_SHORT).show();
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
                                    startActivity(new Intent(LoginActivity.this, DashboardAdminActivity.class));
                                }
                                else {
                                    startActivity(new Intent(LoginActivity.this, DashboardUserActivity.class));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NoDataResponse> call, Throwable t) {
                        if (role.equals("admin")){
                            startActivity(new Intent(LoginActivity.this, DashboardAdminActivity.class));
                        }
                        else {
                            startActivity(new Intent(LoginActivity.this, DashboardUserActivity.class));
                        }
                    }
                });
            }
        });
    }

}