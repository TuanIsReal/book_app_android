package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.ActivityAuthenVerifyCodeBinding;
import com.anhtuan.bookapp.request.AuthenVerifyCodeRequest;
import com.anhtuan.bookapp.response.AuthenVerifyCodeResponse;
import com.anhtuan.bookapp.response.NoDataResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenVerifyCodeActivity extends AppCompatActivity {

    ActivityAuthenVerifyCodeBinding binding;
    String verifyCode;
    boolean iSendAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityAuthenVerifyCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int type = intent.getIntExtra("authenType", 1);
        String email =  intent.getStringExtra("email");
        AuthenVerifyCodeRequest request = new AuthenVerifyCodeRequest();
        request.setType(type);
        request.setEmail(email);
        request.setUserId("");

        binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()){
                    request.setCode(verifyCode);
                    userApi.authenVerifyCode(request).enqueue(new Callback<AuthenVerifyCodeResponse>() {
                        @Override
                        public void onResponse(Call<AuthenVerifyCodeResponse> call, Response<AuthenVerifyCodeResponse> response) {
                            if (response.body().getCode() == 121){
                                Toast.makeText(AuthenVerifyCodeActivity.this, "Mã xác nhận chưa chính xác", Toast.LENGTH_SHORT).show();
                            }
                            if (response.body().getCode() == 100){
                                Intent intent1  = new Intent();
                                intent1.putExtra("userId", response.body().getData());
                               setResult(RESULT_OK, intent1);
                               finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthenVerifyCodeResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });

        binding.sendAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iSendAgain){
                    iSendAgain = true;
                    binding.sendAgainBtn.setText("Đang gửi");
                    userApi.forgotPassword(email).enqueue(new Callback<NoDataResponse>() {
                        @Override
                        public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                            if (!Objects.isNull(response.body())){
                                if (response.body().getCode() == 100){
                                    binding.sendAgainBtn.setText("Đã gửi");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<NoDataResponse> call, Throwable t) {
                            binding.sendAgainBtn.setText("Lỗi");
                        }
                    });
                }
            }
        });

        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private boolean validateData(){
        verifyCode = binding.verifyCodeEt.getText().toString();
        if (verifyCode.isBlank()){
            Toast.makeText(AuthenVerifyCodeActivity.this, "Chưa nhập mã", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}