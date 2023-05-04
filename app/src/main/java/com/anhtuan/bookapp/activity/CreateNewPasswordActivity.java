package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.databinding.ActivityCreateNewPasswordBinding;
import com.anhtuan.bookapp.response.NoDataResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewPasswordActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 10008;

    ActivityCreateNewPasswordBinding binding;
    String newPassword, confirmPassword, userId;
    boolean authen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int type = intent.getIntExtra("authenType", 1);
        String email =  intent.getStringExtra("email");

        Intent intent1 = new Intent(this, AuthenVerifyCodeActivity.class);
        intent1.putExtra("authenType", type);
        intent1.putExtra("email", email);
        startActivityForResult(intent1, REQUEST_CODE);


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()){
                    userApi.createNewPassword(userId, newPassword).enqueue(new Callback<NoDataResponse>() {
                        @Override
                        public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                            if (response.body() != null && response.body().getCode() == 100){
                                Toast.makeText(CreateNewPasswordActivity.this, "Tạo mật khẩu mới thành công", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }

                        }

                        @Override
                        public void onFailure(Call<NoDataResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private boolean validateData(){
        newPassword = binding.newPasswordEt.getText().toString();
        confirmPassword = binding.confirmPasswordEt.getText().toString();

        if (newPassword.isBlank() || confirmPassword.isBlank()){
            Toast.makeText(CreateNewPasswordActivity.this, "Không được để trống", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPassword.equals(confirmPassword)){
            Toast.makeText(CreateNewPasswordActivity.this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && !Objects.isNull(data)){
            userId = data.getStringExtra("userId");
        } else {
            onBackPressed();
        }
    }
}