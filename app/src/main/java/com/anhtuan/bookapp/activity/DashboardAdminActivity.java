package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.CategoryApi.categoryApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.adapter.AdapterCategory;
import com.anhtuan.bookapp.databinding.ActivityDashboardAdminBinding;
import com.anhtuan.bookapp.domain.Category;
import com.anhtuan.bookapp.response.CategoriesResponse;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardAdminActivity extends AppCompatActivity {

    private ActivityDashboardAdminBinding binding;

    private ArrayList<Category> categories;

    private AdapterCategory adapterCategory;
    public static final long TIME_INTERVAL = 3000;
    long backPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");

        loadTitle(userId);

        loadCategories("");

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(sharedPreferences, userId);
            }
        });

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadCategories(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, CategoryAddActivity.class));
            }
        });

        binding.manageBookTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(DashboardAdminActivity.this, ManageBookActivity.class));
            }
        });

        binding.manageRequestTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(DashboardAdminActivity.this, ManageRequestBookActivity.class));
            }
        });

        binding.manageIncomeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(DashboardAdminActivity.this, IncomeAdminActivity.class));
            }
        });

    }

    private void loadTitle(String userId){
        if (userId.equals("")){
            binding.subTitileTv.setText("UserId null");
        }
        else {
            userApi.getUserInfo(userId).enqueue(new Callback<GetUserInfoResponse>() {
                @Override
                public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                    GetUserInfoResponse getUserInfoResponse = response.body();
                    binding.subTitileTv.setText(getUserInfoResponse.getData().getName());
                }

                @Override
                public void onFailure(Call<GetUserInfoResponse> call, Throwable t) {
                    Toast.makeText(DashboardAdminActivity.this, ""+ t, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void logout(SharedPreferences sharedPreferences, String userId){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", "");
        editor.apply();

        userApi.logout(userId).enqueue(new Callback<NoDataResponse>() {
            @Override
            public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                finish();
                startActivity(new Intent(DashboardAdminActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<NoDataResponse> call, Throwable t) {
                Toast.makeText(DashboardAdminActivity.this, ""+ t, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadCategories(String text){
        categoryApi.searchCategory(text).enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                CategoriesResponse categoriesResponse = response.body();
                if (categoriesResponse.getCode() == 100){
                    categories = categoriesResponse.getData();
                }
                adapterCategory = new AdapterCategory(DashboardAdminActivity.this, categories);
                binding.categoriesRv.setAdapter(adapterCategory);
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Toast.makeText(DashboardAdminActivity.this, ""+ t, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(DashboardAdminActivity.this, "Quay lại lần nữa để thoát", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }
}