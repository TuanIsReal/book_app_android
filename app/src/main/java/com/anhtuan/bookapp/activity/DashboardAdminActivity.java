package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.CategoryApi.categoryApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.adapter.AdapterCategory;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.databinding.ActivityDashboardAdminBinding;
import com.anhtuan.bookapp.domain.Category;
import com.anhtuan.bookapp.response.CategoriesResponse;
import com.anhtuan.bookapp.response.CheckUserInfoResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import java.util.ArrayList;

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

        loadTitle();

        loadCategories("");

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
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

        binding.manageUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(DashboardAdminActivity.this, ManageUserActivity.class));
            }
        });

        binding.manageStatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(DashboardAdminActivity.this, StatisticalActivity.class));
            }
        });

        binding.manageWarningTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(DashboardAdminActivity.this, ManageWarningActivity.class));
            }
        });

        binding.settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, AdminSettingActivity.class));
            }
        });

    }

    private void loadTitle(){
        userApi.checkUserInfo().enqueue(new RetrofitCallBack<CheckUserInfoResponse>() {
            @Override
            public void onSuccess(CheckUserInfoResponse response) {
                binding.subTitileTv.setText(response.getData().getName());
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void logout(){
        userApi.logout().enqueue(new RetrofitCallBack<NoDataResponse>() {
            @Override
            public void onSuccess(NoDataResponse response) {
                TokenManager.getInstance().deleteToken();
                AccountManager.getInstance().logoutAccount();
                startActivity(new Intent(DashboardAdminActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    public void loadCategories(String text){
        categoryApi.searchCategory(text).enqueue(new RetrofitCallBack<CategoriesResponse>() {
            @Override
            public void onSuccess(CategoriesResponse response) {
                if (response.getCode() == 100){
                    categories = response.getData();
                }
                adapterCategory = new AdapterCategory(DashboardAdminActivity.this, categories);
                binding.categoriesRv.setAdapter(adapterCategory);
            }

            @Override
            public void onFailure(String errorMessage) {

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