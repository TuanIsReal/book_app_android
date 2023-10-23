package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookReviewApi.bookReviewApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.databinding.ActivityNewBookReviewBinding;
import com.anhtuan.bookapp.request.AddBookReviewRequest;
import com.anhtuan.bookapp.response.NoDataResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewBookReviewActivity extends AppCompatActivity {

    ActivityNewBookReviewBinding binding;
    String userId;
    String bookId;
    double scoreReview;
    String reviewContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityNewBookReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        bookId = intent.getStringExtra("bookId");

        binding.confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()){
                    AddBookReviewRequest request = new AddBookReviewRequest(bookId, userId, scoreReview, reviewContent);
                    bookReviewApi.addBookReview(request).enqueue(new Callback<NoDataResponse>() {
                        @Override
                        public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                            if (response.body() != null){
                                NoDataResponse responseBody = response.body();
                                if (responseBody.getCode() == 106){
                                    Toast.makeText(NewBookReviewActivity.this, "Không tìm thấy user", Toast.LENGTH_SHORT).show();
                                }
                                if (responseBody.getCode() == 118){
                                    Toast.makeText(NewBookReviewActivity.this, "Bạn đã từng đánh giá truyện rồi", Toast.LENGTH_SHORT).show();
                                }
                                if (responseBody.getCode() == 109){
                                    Toast.makeText(NewBookReviewActivity.this, "Không tìm thấy truyện", Toast.LENGTH_SHORT).show();
                                }
                                if (responseBody.getCode() == 100){
                                    Toast.makeText(NewBookReviewActivity.this, "Đánh giá truyện thành công", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<NoDataResponse> call, Throwable t) {
                            Toast.makeText(NewBookReviewActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        binding.backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private boolean validateData(){
        scoreReview = binding.ratingBar.getRating();
        reviewContent = binding.reviewBodyEt.getText().toString().trim();

        if (scoreReview <= 0){
            Toast.makeText(NewBookReviewActivity.this, "Bạn chưa chọn điểm đánh giá", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (reviewContent.isBlank() && reviewContent.length() < 20){
            Toast.makeText(NewBookReviewActivity.this, "Nội dung tối thiểu 20 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}