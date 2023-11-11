package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.databinding.ActivityStatisticalBinding;
import com.anhtuan.bookapp.response.NoDataResponse;

public class StatisticalActivity extends AppCompatActivity {
    ActivityStatisticalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityStatisticalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                logout();
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

    private void logout(){
        userApi.logout().enqueue(new RetrofitCallBack<NoDataResponse>() {
            @Override
            public void onSuccess(NoDataResponse response) {
                TokenManager.getInstance().deleteToken();
                AccountManager.getInstance().logoutAccount();
                startActivity(new Intent(StatisticalActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}