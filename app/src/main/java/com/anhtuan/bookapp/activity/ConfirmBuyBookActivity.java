package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.anhtuan.bookapp.databinding.ActivityConfirmBuyBookBinding;
import com.anhtuan.bookapp.response.BaseResponse;
import com.anhtuan.bookapp.response.GetUserInfoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmBuyBookActivity extends AppCompatActivity {

    String userId;
    String bookId;
    String bookName;
    String author;
    int price;
    int balance;
    ActivityConfirmBuyBookBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBuyBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        bookId = intent.getStringExtra("bookId");
        bookName = intent.getStringExtra("bookName");
        author = intent.getStringExtra("author");
        price = intent.getIntExtra("price", 0);

        binding.bookName.setText(bookName);
        binding.author.setText(author);
        binding.price.setText(String.valueOf(price));

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        if (userId != null){
            userApi.getUserInfo(userId).enqueue(new Callback<GetUserInfoResponse>() {
                @Override
                public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                    if (response != null){
                        GetUserInfoResponse responseBody = response.body();
                        if (responseBody.getCode() == 100){
                            balance = responseBody.getData().getPoint();
                            binding.balanceNum.setText(String.valueOf(balance));
                            binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (balance < price){
                                        Toast.makeText(ConfirmBuyBookActivity.this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        buyBook();
                                    }
                                }
                            });
                        } else if (responseBody.getCode() == 106) {
                            binding.confirmTv.setText("Lấy thông tin người dùng bị lỗi");
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetUserInfoResponse> call, Throwable t) {
                    Toast.makeText(ConfirmBuyBookActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void buyBook(){
        purchasedBookApi.buyBook(userId, bookId).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response != null){
                    BaseResponse responseBody = response.body();
                    if (responseBody.getCode() == 106){
                        Toast.makeText(ConfirmBuyBookActivity.this, "Lấy thông tin người dùng bị lỗi", Toast.LENGTH_SHORT).show();
                    } else if (responseBody.getCode() == 109) {
                        Toast.makeText(ConfirmBuyBookActivity.this, "Lấy thông tin sách bị lỗi", Toast.LENGTH_SHORT).show();
                    } else if (responseBody.getCode() == 112) {
                        Toast.makeText(ConfirmBuyBookActivity.this, "Sách đã mua", Toast.LENGTH_SHORT).show();
                    } else if (responseBody.getCode() == 111) {
                        Toast.makeText(ConfirmBuyBookActivity.this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
                    } else if (responseBody.getCode() == 100) {
                        Toast.makeText(ConfirmBuyBookActivity.this, "Mua thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(ConfirmBuyBookActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}