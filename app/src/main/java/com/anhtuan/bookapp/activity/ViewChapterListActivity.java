package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookChapterApi.bookChapterApi;
import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.anhtuan.bookapp.adapter.AdapterViewChapterList;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.ActivityViewChapterListBinding;
import com.anhtuan.bookapp.domain.BookChapter;
import com.anhtuan.bookapp.domain.PurchasedBook;
import com.anhtuan.bookapp.response.GetBookChapterListResponse;
import com.anhtuan.bookapp.response.GetPurchasedBookResponse;
import java.util.List;

public class ViewChapterListActivity extends AppCompatActivity implements AdapterViewChapterList.BookChapterItemListener {

    ActivityViewChapterListBinding binding;
    List<BookChapter> bookChapters;
    AdapterViewChapterList adapterViewChapterList;
    String bookId;
    int lastReadChapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityViewChapterListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
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
        purchasedBookApi.getPurchasedBook(bookId).enqueue(new RetrofitCallBack<GetPurchasedBookResponse>() {
            @Override
            public void onSuccess(GetPurchasedBookResponse response) {
                if (response != null){
                    if (response.getCode() == 106){
                        Toast.makeText(ViewChapterListActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    } else if (response.getCode() == 112) {
                        loadBookChapterList();
                    } else if (response.getCode() == 100) {
                        PurchasedBook purchasedBook = response.getData();
                        lastReadChapter = purchasedBook.getLastReadChapter();
                        loadBookChapterList();
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadBookChapterList(){
        bookChapterApi.getBookChapterList(bookId).enqueue(new RetrofitCallBack<GetBookChapterListResponse>() {
            @Override
            public void onSuccess(GetBookChapterListResponse response) {
                if (response != null){
                    if (response.getCode() == 100 && response.getData() != null) {
                        bookChapters = response.getData();
                        String chapterSize = String.valueOf(bookChapters.size());
                        binding.chapterNum.setText("(" + chapterSize +")");
                        adapterViewChapterList = new AdapterViewChapterList(ViewChapterListActivity.this, bookChapters, lastReadChapter);
                        binding.bookChaptersRv.setAdapter(adapterViewChapterList);
                        adapterViewChapterList.setBookChapterItemListener(ViewChapterListActivity.this);
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

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