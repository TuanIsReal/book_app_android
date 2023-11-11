package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.ActivityUpdatePasswordBinding;
import com.anhtuan.bookapp.response.NoDataResponse;

public class UpdatePasswordActivity extends AppCompatActivity {

    ActivityUpdatePasswordBinding binding;
    String oldPassword, newPassword, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityUpdatePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()){
                    userApi.updatePassword(oldPassword, newPassword).enqueue(new RetrofitCallBack<NoDataResponse>() {
                        @Override
                        public void onSuccess(NoDataResponse response) {
                            if (response != null && response.getCode() == 100){
                                Toast.makeText(UpdatePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                            if (response != null && response.getCode() == 123){
                                Toast.makeText(UpdatePasswordActivity.this, "Mật kẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {

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
        oldPassword = binding.oldPasswordEt.getText().toString();
        newPassword = binding.newPasswordEt.getText().toString();
        confirmPassword = binding.confirmPasswordEt.getText().toString();

        if (oldPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()){
            Toast.makeText(UpdatePasswordActivity.this, "Không được để trống", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPassword.equals(confirmPassword)){
            Toast.makeText(UpdatePasswordActivity.this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (newPassword.equals(oldPassword)){
            Toast.makeText(UpdatePasswordActivity.this, "Mật khẩu mới phải khác mật khẩu cũ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}