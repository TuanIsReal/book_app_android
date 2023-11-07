package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;
import static com.anhtuan.bookapp.api.WarningApi.warningApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterManageWarning;
import com.anhtuan.bookapp.databinding.ActivityManageWarningBinding;
import com.anhtuan.bookapp.domain.WarningChapter;
import com.anhtuan.bookapp.response.GetWarningListResponse;
import com.anhtuan.bookapp.response.NoDataResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageWarningActivity extends AppCompatActivity {

    ActivityManageWarningBinding binding;
    List<WarningChapter> warningChapterList;
    AdapterManageWarning adapterManageWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityManageWarningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");

        loadWarningChapter();

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWarningChapter();
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
                startActivity(new Intent(ManageWarningActivity.this, DashboardAdminActivity.class));
            }
        });

        binding.manageBookTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ManageWarningActivity.this, ManageBookActivity.class));
            }
        });

        binding.manageStatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ManageWarningActivity.this, StatisticalActivity.class));
            }
        });

        binding.manageRequestTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ManageWarningActivity.this, ManageRequestBookActivity.class));
            }
        });
    }

    private void loadWarningChapter() {
        warningApi.getWarningList().enqueue(new Callback<GetWarningListResponse>() {
            @Override
            public void onResponse(Call<GetWarningListResponse> call, Response<GetWarningListResponse> response) {
                binding.swipeRefresh.setRefreshing(false);
                if (response.body() != null){
                    GetWarningListResponse responseBody = response.body();
                    if (responseBody.getCode() == 100){
                        warningChapterList = responseBody.getData();
                        loadWarningChapterRecycleView();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetWarningListResponse> call, Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void loadWarningChapterRecycleView() {
        adapterManageWarning = new AdapterManageWarning(ManageWarningActivity.this, warningChapterList);
        binding.warningChapterRv.setAdapter(adapterManageWarning);
    }


    private void logout(SharedPreferences sharedPreferences, String userId){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", "");
        editor.apply();

        userApi.logout(userId).enqueue(new Callback<NoDataResponse>() {
            @Override
            public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                finish();
                startActivity(new Intent(ManageWarningActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<NoDataResponse> call, Throwable t) {
                Toast.makeText(ManageWarningActivity.this, ""+ t, Toast.LENGTH_LONG).show();
            }
        });
    }
}