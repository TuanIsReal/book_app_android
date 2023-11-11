package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookApi.bookApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import com.anhtuan.bookapp.adapter.AdapterSearchBookUser;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.ActivitySearchBookUserBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.SearchBookResponse;

import java.util.ArrayList;

public class SearchBookUserActivity extends AppCompatActivity {

    private ActivitySearchBookUserBinding binding;
    private ArrayList<Book> books;
    private AdapterSearchBookUser adapterSearchBookUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
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
        bookApi.searchBook(text).enqueue(new RetrofitCallBack<SearchBookResponse>() {
            @Override
            public void onSuccess(SearchBookResponse response) {
                if (response.getCode() == 100){
                    books = response.getData();
                    loadAdapter(books);
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadAdapter(ArrayList<Book> books){
        adapterSearchBookUser = new AdapterSearchBookUser(SearchBookUserActivity.this, books);
        binding.booksRv.setAdapter(adapterSearchBookUser);
    }

}