package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.databinding.ActivityStatisticalBinding;
import com.anhtuan.bookapp.response.NoDataResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticalActivity extends AppCompatActivity {
    ActivityStatisticalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityStatisticalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");

        binding.incomeWriterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticalActivity.this, StatisticalDetailActivity.class);
                intent.putExtra("typeRanking", 1);
                startActivity(intent);
            }
        });

        binding.spendPointTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticalActivity.this, StatisticalDetailActivity.class);
                intent.putExtra("typeRanking", 2);
                startActivity(intent);
            }
        });

        binding.rechargeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticalActivity.this, StatisticalDetailActivity.class);
                intent.putExtra("typeRanking", 3);
                startActivity(intent);
            }
        });

        binding.bookIncomeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticalActivity.this, StatisticalDetailActivity.class);
                intent.putExtra("typeRanking", 4);
                startActivity(intent);
            }
        });

        binding.incomeAdminTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticalActivity.this, IncomeAdminActivity.class);
                startActivity(intent);
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(sharedPreferences, userId);
            }
        });

        binding.manageCategoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(StatisticalActivity.this, DashboardAdminActivity.class));
            }
        });

        binding.manageBookTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(StatisticalActivity.this, ManageBookActivity.class));
            }
        });

        binding.manageRequestTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(StatisticalActivity.this, ManageRequestBookActivity.class));
            }
        });
    }

    private void logout(SharedPreferences sharedPreferences, String userId){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", "");
        editor.apply();

        userApi.logout(userId).enqueue(new Callback<NoDataResponse>() {
            @Override
            public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                finish();
                startActivity(new Intent(StatisticalActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<NoDataResponse> call, Throwable t) {
                Toast.makeText(StatisticalActivity.this, ""+ t, Toast.LENGTH_LONG).show();
            }
        });
    }
}