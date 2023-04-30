package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterBookAdmin;
import com.anhtuan.bookapp.databinding.ActivityManageBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.response.SearchBookResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBookActivity extends AppCompatActivity {

    private ActivityManageBookBinding binding;

    private ArrayList<Book> books;

    private AdapterBookAdmin adapterBookAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");

        loadTitle(userId);
        loadBooks("");

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.dashboardAdminTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageBookActivity.this, DashboardAdminActivity.class));
            }
        });

        binding.addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageBookActivity.this, BookAddActivity.class));
            }
        });

    }

    private void loadBooks(String text) {
        bookApi.searchBook(text).enqueue(new Callback<SearchBookResponse>() {
            @Override
            public void onResponse(Call<SearchBookResponse> call, Response<SearchBookResponse> response) {
                SearchBookResponse searchBookResponse = response.body();
                if (searchBookResponse.getCode() == 100){
                    books = searchBookResponse.getData();
                    adapterBookAdmin = new AdapterBookAdmin(ManageBookActivity.this, books);
                    binding.booksRv.setAdapter(adapterBookAdmin);
                }else {
                    Toast.makeText(ManageBookActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchBookResponse> call, Throwable t) {
                Toast.makeText(ManageBookActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTitle(String userId) {
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
                    Toast.makeText(ManageBookActivity.this, ""+ t, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}