package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookApi.bookApi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterBookAdmin;
import com.anhtuan.bookapp.adapter.AdapterSearchBookUser;
import com.anhtuan.bookapp.databinding.ActivitySearchBookUserBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.SearchBookResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchBookUserActivity extends AppCompatActivity {

    private ActivitySearchBookUserBinding binding;
    private ArrayList<Book> books;
    private AdapterSearchBookUser adapterSearchBookUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBookUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cancelSearchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1){
                    loadBooks(s.toString());
                } else {
                    loadAdapter(new ArrayList<>());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                    loadAdapter(books);
                }else {
                    Toast.makeText(SearchBookUserActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchBookResponse> call, Throwable t) {
                Toast.makeText(SearchBookUserActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAdapter(ArrayList<Book> books){
        adapterSearchBookUser = new AdapterSearchBookUser(SearchBookUserActivity.this, books);
        binding.booksRv.setAdapter(adapterSearchBookUser);
    }

}