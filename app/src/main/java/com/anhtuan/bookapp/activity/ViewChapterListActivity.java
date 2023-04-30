package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookChapterApi.bookChapterApi;
import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.anhtuan.bookapp.adapter.AdapterBookChapterList;
import com.anhtuan.bookapp.adapter.AdapterViewChapterList;
import com.anhtuan.bookapp.databinding.ActivityViewChapterListBinding;
import com.anhtuan.bookapp.domain.BookChapter;
import com.anhtuan.bookapp.domain.PurchasedBook;
import com.anhtuan.bookapp.response.GetBookChapterListResponse;
import com.anhtuan.bookapp.response.GetPurchasedBookResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewChapterListActivity extends AppCompatActivity implements AdapterViewChapterList.BookChapterItemListener {

    ActivityViewChapterListBinding binding;
    List<BookChapter> bookChapters;
    AdapterViewChapterList adapterViewChapterList;
    String userId;
    String bookId;
    int lastReadChapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewChapterListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        bookId = intent.getStringExtra("bookId");

        loadPurchasedBook();

        binding.chapterNumText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void loadPurchasedBook(){
        purchasedBookApi.getPurchasedBook(bookId, userId).enqueue(new Callback<GetPurchasedBookResponse>() {
            @Override
            public void onResponse(Call<GetPurchasedBookResponse> call, Response<GetPurchasedBookResponse> response) {
                if (response != null){
                    GetPurchasedBookResponse responseBody = response.body();
                    if (responseBody.getCode() == 106){
                        Toast.makeText(ViewChapterListActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    } else if (responseBody.getCode() == 112) {
                        loadBookChapterList();
                    } else if (responseBody.getCode() == 100) {
                        PurchasedBook purchasedBook = responseBody.getData();
                        lastReadChapter = purchasedBook.getLastReadChapter();
                        loadBookChapterList();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPurchasedBookResponse> call, Throwable t) {
                Toast.makeText(ViewChapterListActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBookChapterList(){
        bookChapterApi.getBookChapterList(bookId).enqueue(new Callback<GetBookChapterListResponse>() {
            @Override
            public void onResponse(Call<GetBookChapterListResponse> call, Response<GetBookChapterListResponse> response) {
                if (response.body() != null){
                    GetBookChapterListResponse responseBody = response.body();
                    if (responseBody.getCode() == 106){

                    }
                    if (responseBody.getCode() == 100 && responseBody.getData() != null) {
                        bookChapters = responseBody.getData();
                        String chapterSize = String.valueOf(bookChapters.size());
                        binding.chapterNum.setText("(" + chapterSize +")");
                        adapterViewChapterList = new AdapterViewChapterList(ViewChapterListActivity.this, bookChapters, lastReadChapter);
                        binding.bookChaptersRv.setAdapter(adapterViewChapterList);
                        adapterViewChapterList.setBookChapterItemListener(ViewChapterListActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBookChapterListResponse> call, Throwable t) {
                Toast.makeText(ViewChapterListActivity.this, "" +t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.putExtra("chapterNumber", bookChapters.get(position).getChapterNumber());
        setResult(RESULT_OK, intent);
        finish();
    }
}