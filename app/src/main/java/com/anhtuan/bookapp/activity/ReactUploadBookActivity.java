package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.STFApi.stfApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.ActivityReactUploadBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.GetUsernameResponse;
import com.anhtuan.bookapp.response.ImageResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReactUploadBookActivity extends AppCompatActivity {

    ActivityReactUploadBookBinding binding;
    Book bookRequestUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityReactUploadBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        bookRequestUp = (Book) intent.getSerializableExtra("bookRequestUp");

        if (bookRequestUp != null){
            loadBookRequestUpInfo();
        }


    }

    private void loadBookRequestUpInfo() {

        binding.bookName.setText(bookRequestUp.getBookName());
        userApi.getUsername(bookRequestUp.getAuthor()).enqueue(new Callback<GetUsernameResponse>() {
            @Override
            public void onResponse(Call<GetUsernameResponse> call, Response<GetUsernameResponse> response) {
                if (response.body().getCode() == 100){
                    binding.author.setText(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<GetUsernameResponse> call, Throwable t) {

            }
        });
        binding.introduction.setText(bookRequestUp.getIntroduction());
        binding.price.setText(String.valueOf(bookRequestUp.getBookPrice()));

        if (bookRequestUp.getBookImage() != null){
            loadBookImage();

            binding.acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reactBookRequestUp(Constant.ReactUpBookRequest.ACCEPT);
                }
            });

            binding.rejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reactBookRequestUp(Constant.ReactUpBookRequest.REJECT);
                }
            });

        } else {
            binding.progressBar.setVisibility(View.GONE);
        }

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private void loadBookImage() {
        stfApi.getBookImage(bookRequestUp.getBookImage()).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.body().getCode() == 100){
                    String path = Constant.IP_SERVER_IMAGE + response.body().getData();
                    Glide.with(ReactUploadBookActivity.this)
                            .load(path)
                            .signature(new ObjectKey(path
                            ))
                            .into(binding.imageView);
                }
            }
            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void reactBookRequestUp(int action){
        bookApi.reactBookRequestUp(bookRequestUp.getId(), action).enqueue(new Callback<NoDataResponse>() {
            @Override
            public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                if (response.body() != null){
                    NoDataResponse responseBody = response.body();
                    if (responseBody.getCode() == 109) {
                        Toast.makeText(ReactUploadBookActivity.this, "Truyện không tồn tại", Toast.LENGTH_SHORT).show();
                    } else if (responseBody.getCode() == 100) {
                        Intent intent = new Intent();
                        intent.putExtra("action", action);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<NoDataResponse> call, Throwable t) {
                Toast.makeText(ReactUploadBookActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}