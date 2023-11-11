package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import com.anhtuan.bookapp.adapter.AdapterBookAdmin;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.databinding.ActivityManageBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.CheckUserInfoResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.response.SearchBookResponse;

import java.util.ArrayList;


public class ManageBookActivity extends AppCompatActivity {

    private ActivityManageBookBinding binding;

    private ArrayList<Book> books;

    private AdapterBookAdmin adapterBookAdmin;
    public static final long TIME_INTERVAL = 3000;
    long backPressed;
    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityManageBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadTitle();
        loadBooks(text);

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
                loadBooks(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBooks(text);
            }
        });

        binding.dashboardAdminTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageBookActivity.this, DashboardAdminActivity.class));
                finish();
            }
        });

        binding.addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageBookActivity.this, BookAddActivity.class));
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
                startActivity(new Intent(ManageBookActivity.this, ManageRequestBookActivity.class));
                finish();
            }
        });

        binding.manageStatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageBookActivity.this, StatisticalActivity.class));
                finish();
            }
        });
    }

    private void loadBooks(String text) {
        bookApi.searchBook(text).enqueue(new RetrofitCallBack<SearchBookResponse>() {
            @Override
            public void onSuccess(SearchBookResponse response) {
                if (response.getCode() == 100){
                    books = response.getData();
                    adapterBookAdmin = new AdapterBookAdmin(ManageBookActivity.this, books);
                    binding.booksRv.setAdapter(adapterBookAdmin);
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadTitle() {
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
                startActivity(new Intent(ManageBookActivity.this, MainActivity.class));
                finish();
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
            Toast.makeText(ManageBookActivity.this, "Quay lại lần nữa để thoát", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }
}