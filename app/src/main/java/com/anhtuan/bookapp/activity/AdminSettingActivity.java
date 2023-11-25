package com.anhtuan.bookapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.databinding.ActivityAdminSettingBinding;

public class AdminSettingActivity extends AppCompatActivity {

    ActivityAdminSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityAdminSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.returnBtn.setOnClickListener(v -> onBackPressed());

        binding.bannedWordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSettingActivity.this, SettingBannedWordActivity.class));
            }
        });

        binding.changePasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSettingActivity.this, UpdatePasswordActivity.class));
            }
        });

        binding.manageReportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSettingActivity.this, ManageReportActivity.class));
            }
        });
    }
}