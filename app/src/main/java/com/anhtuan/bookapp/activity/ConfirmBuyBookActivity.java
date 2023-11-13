package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.databinding.ActivityConfirmBuyBookBinding;
import com.anhtuan.bookapp.response.BaseResponse;
import com.anhtuan.bookapp.response.CheckUserInfoResponse;
import com.anhtuan.bookapp.response.GetUsernameResponse;

public class ConfirmBuyBookActivity extends AppCompatActivity {

    String bookId;
    String bookName;
    String author;
    int price;
    int balance;
    ActivityConfirmBuyBookBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityConfirmBuyBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");
        bookName = intent.getStringExtra("bookName");
        author = intent.getStringExtra("author");
        price = intent.getIntExtra("price", 0);

        binding.bookName.setText(bookName);
        userApi.getUsername(author).enqueue(new RetrofitCallBack<GetUsernameResponse>() {
            @Override
            public void onSuccess(GetUsernameResponse response) {
                if (response.getCode() == 100){
                    binding.author.setText(response.getData());
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
        binding.price.setText(String.valueOf(price));

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        userApi.checkUserInfo().enqueue(new RetrofitCallBack<CheckUserInfoResponse>() {
            @Override
            public void onSuccess(CheckUserInfoResponse response) {
                if (response.getCode() == 122){
                    AccountManager.getInstance().logoutAccount();
                    TokenManager.getInstance().deleteToken();
                    startActivity(new Intent(ConfirmBuyBookActivity.this, MainActivity.class));
                    finish();
                } else if (response.getCode() == 100){
                    balance = response.getData().getPoint();
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
                } else if (response.getCode() == 106) {
                    binding.confirmTv.setText("Lấy thông tin người dùng bị lỗi");
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

    }

    private void buyBook(){
        purchasedBookApi.buyBook(bookId).enqueue(new RetrofitCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response != null){
                    if (response.getCode() == 106){
                        Toast.makeText(ConfirmBuyBookActivity.this, "Lấy thông tin người dùng bị lỗi", Toast.LENGTH_SHORT).show();
                    } else if (response.getCode() == 109) {
                        Toast.makeText(ConfirmBuyBookActivity.this, "Lấy thông tin sách bị lỗi", Toast.LENGTH_SHORT).show();
                    } else if (response.getCode() == 112) {
                        Toast.makeText(ConfirmBuyBookActivity.this, "Sách đã mua", Toast.LENGTH_SHORT).show();
                    } else if (response.getCode() == 111) {
                        Toast.makeText(ConfirmBuyBookActivity.this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
                    } else if (response.getCode() == 100) {
                        Toast.makeText(ConfirmBuyBookActivity.this, "Mua thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}