package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.databinding.ActivityUpdateNameBinding;
import com.anhtuan.bookapp.response.NoDataResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateNameActivity extends AppCompatActivity {

    ActivityUpdateNameBinding binding;
    String name, password, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityUpdateNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");


        binding.confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()){
                    userApi.updateName(userId, password, name).enqueue(new Callback<NoDataResponse>() {
                        @Override
                        public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                            if (response.body() != null && response.body().getCode() == 100){
                                Toast.makeText(UpdateNameActivity.this, "Đổi tên thành công", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }

                            if (response.body() != null && response.body().getCode() == 106){
                                Toast.makeText(UpdateNameActivity.this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<NoDataResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private boolean validateData(){
        name = binding.newNameEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString();

        if (name.isBlank() || password.isBlank()){
            Toast.makeText(UpdateNameActivity.this, "Không được để trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.length() > 20){
            Toast.makeText(UpdateNameActivity.this, "Tên không vượt quá 20 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}