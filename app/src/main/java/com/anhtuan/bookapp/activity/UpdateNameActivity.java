package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.ActivityUpdateNameBinding;
import com.anhtuan.bookapp.response.NoDataResponse;

public class UpdateNameActivity extends AppCompatActivity {

    ActivityUpdateNameBinding binding;
    String name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityUpdateNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()){
                    userApi.updateName(password, name).enqueue(new RetrofitCallBack<NoDataResponse>() {
                        @Override
                        public void onSuccess(NoDataResponse response) {
                            if (response != null && response.getCode() == 100){
                                Toast.makeText(UpdateNameActivity.this, "Đổi tên thành công", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }

                            if (response != null && response.getCode() == 123){
                                Toast.makeText(UpdateNameActivity.this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
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