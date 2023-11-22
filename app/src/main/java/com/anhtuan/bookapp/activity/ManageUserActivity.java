package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.databinding.ActivityManageUserBinding;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.response.SearchUserResponse;

import java.util.List;

public class ManageUserActivity extends AppCompatActivity {

    ActivityManageUserBinding binding;
    List<User> users;
    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityManageUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    text = s.toString();
                } else {
                    text = "";
                }
                loadUsers(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUsers(text);
            }
        });

        binding.manageCategoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageUserActivity.this, DashboardAdminActivity.class));
                finish();
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        binding.manageRequestTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageUserActivity.this, ManageRequestBookActivity.class));
                finish();
            }
        });

        binding.manageStatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageUserActivity.this, StatisticalActivity.class));
                finish();
            }
        });
    }

    private void loadUsers(String text) {
        userApi.searchUser(text).enqueue(new RetrofitCallBack<SearchUserResponse>() {
            @Override
            public void onSuccess(SearchUserResponse response) {
                binding.swipeRefresh.setRefreshing(false);
                if (response != null) {
                    if (response.getCode() == 100) {
                        users = response.getData();
                    }
                }
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
                startActivity(new Intent(ManageUserActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}