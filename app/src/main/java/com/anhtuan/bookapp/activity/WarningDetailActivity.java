package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookChapterApi.bookChapterApi;
import static com.anhtuan.bookapp.api.WarningApi.warningApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.ActivityWarningDetailBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.domain.BookChapter;
import com.anhtuan.bookapp.response.GetChapterContentResponse;
import com.anhtuan.bookapp.response.NoDataResponse;

public class WarningDetailActivity extends AppCompatActivity {

    ActivityWarningDetailBinding binding;
    Book book;
    Book bookReport;
    BookChapter bookChapter;
    BookChapter bookChapterReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityWarningDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");
        bookReport = (Book) intent.getSerializableExtra("bookReport");
        bookChapter = (BookChapter) intent.getSerializableExtra("chapter");
        bookChapterReport = (BookChapter) intent.getSerializableExtra("chapterReport");

        setWarningDetail();
    }

    private void setWarningDetail() {
        binding.bookNameCheck.setText(bookReport.getBookName());
        binding.chapterNameCheck.setText(String.valueOf(bookChapterReport.getChapterNumber()) + " - " + bookChapterReport.getChapterName());
        binding.bookName.setText(book.getBookName());
        binding.author.setText(book.getAuthor());
        binding.chapterName.setText(String.valueOf(bookChapter.getChapterNumber()) + " - " + bookChapter.getChapterName());

        bookChapterApi.getChapterContentById(bookChapter.getId()).enqueue(new RetrofitCallBack<GetChapterContentResponse>() {
            @Override
            public void onSuccess(GetChapterContentResponse response) {
                if (response.getCode() == 100){
                    binding.content.setText(response.getData().getChapterContent());
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        binding.goodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reactWarning(Constant.ReactWarning.GOOD);
            }
        });

        binding.notGoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reactWarning(Constant.ReactWarning.NOT_GOOD);
            }
        });
    }

    private void reactWarning(int react) {
        warningApi.reactWarning(bookChapter.getId(), react).enqueue(new RetrofitCallBack<NoDataResponse>() {
            @Override
            public void onSuccess(NoDataResponse response) {
                if (response.getCode() == 100){
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}