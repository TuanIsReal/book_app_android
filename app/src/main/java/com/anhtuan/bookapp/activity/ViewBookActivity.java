package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;
import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.adapter.AdapterViewBook;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.ActivityViewBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.domain.PurchasedBook;
import com.anhtuan.bookapp.response.CheckPurchasedBookResponse;
import com.anhtuan.bookapp.response.CheckUserInfoResponse;
import com.anhtuan.bookapp.response.GetPurchasedBookResponse;
import com.anhtuan.bookapp.response.GetUsernameResponse;
import com.anhtuan.bookapp.response.ImageResponse;
import com.anhtuan.bookapp.response.ViewBookResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewBookActivity extends AppCompatActivity {

    private final static int REQUEST_CODE = 100000;
    private ActivityViewBookBinding binding;
    String bookName;
    String categories;
    String author;
    String star;
    String introduction;
    String imageName;
    int price;
    AdapterViewBook adapterViewBook;
    public String bookId;
    boolean isPurchased = false;
    int chapterNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityViewBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");

        userApi.checkUserInfo().enqueue(new RetrofitCallBack<CheckUserInfoResponse>() {
            @Override
            public void onSuccess(CheckUserInfoResponse response) {
                if (response.getCode() == 122 || response.getCode() == 106){
                    AccountManager.getInstance().logoutAccount();
                    TokenManager.getInstance().deleteToken();
                    startActivity(new Intent(ViewBookActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });

        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (bookId == null){
            Toast.makeText(this, "Lấy thông tin sách bị lỗi", Toast.LENGTH_SHORT).show();
        } else {
            purchasedBookApi.checkPurchasedBook(bookId).enqueue(new RetrofitCallBack<CheckPurchasedBookResponse>() {
                @Override
                public void onSuccess(CheckPurchasedBookResponse response) {
                    if (response.getCode() == 100){
                        if (response.getData() == 1) {
                            isPurchased = true;
                        }
                        setVisiButton();
                        getBookInfo();
                        binding.readBookBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openBookChapter();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        }

    }


    private void setBookInfo(){
        binding.bookNameTv.setText(bookName);
        binding.categoriesTv.setText(categories);
        userApi.getUsername(author).enqueue(new RetrofitCallBack<GetUsernameResponse>() {
            @Override
            public void onSuccess(GetUsernameResponse response) {
                if (response.getCode() == 100){
                    binding.authorTv.setText("Tác giả: " + response.getData());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });

        binding.starTv.setText(star);
        binding.priceTv.setText(String.valueOf(price));
        if (imageName != null){
            unAuthApi.getBookImage(imageName).enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                    if (response.body().getCode() == 100){
                        String path = Constant.IP_SERVER_IMAGE + response.body().getData();
                        Glide.with(ViewBookActivity.this)
                                .load(path)
                                .signature(new ObjectKey(path))
                                .into(binding.imageView);
                        Glide.with(ViewBookActivity.this)
                                .load(path)
                                .signature(new ObjectKey(path))
                                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,3)))
                                .into(binding.backgroundImageView);
                    }
                }

                @Override
                public void onFailure(Call<ImageResponse> call, Throwable t) {
                    Log.d("err", "err--fail");
                }
            });
        }



    }

    private void getBookInfo(){
        bookApi.getBookById(bookId).enqueue(new RetrofitCallBack<ViewBookResponse>() {
            @Override
            public void onSuccess(ViewBookResponse response) {
                if (response.getCode() == 109){
                    Toast.makeText(ViewBookActivity.this, "Sach khong ton tai", Toast.LENGTH_SHORT).show();
                } else if (response.getCode() == 100) {
                    Book book = response.getData();
                    bookName = book.getBookName();
                    categories = toStringCategory(book.getBookCategory());
                    author = book.getAuthor();
                    star = String.valueOf(book.getStar());
                    introduction = book.getIntroduction();
                    imageName = book.getBookImage();
                    price = book.getBookPrice();
                    adapterViewBook = new AdapterViewBook(getSupportFragmentManager(), 3, bookId, isPurchased, author);
                    binding.viewPager.setAdapter(adapterViewBook);
                    binding.tab.setupWithViewPager(binding.viewPager);
                    binding.buyBookBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buyBook();
                        }
                    });
                    setBookInfo();
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void buyBook(){
        Intent intent = new Intent(ViewBookActivity.this, ConfirmBuyBookActivity.class);
        intent.putExtra("bookId", bookId);
        intent.putExtra("bookName", bookName);
        intent.putExtra("author", author);
        intent.putExtra("price", price);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            isPurchased = true;
            setVisiButton();
            getBookInfo();
        }
    }

    private void setVisiButton(){
        if (isPurchased) {
            binding.buyBookBtn.setVisibility(View.GONE);
            binding.priceTv.setVisibility(View.GONE);
            binding.readBookBtn.setVisibility(View.VISIBLE);
        } else {
            binding.buyBookBtn.setVisibility(View.VISIBLE);
            binding.priceTv.setVisibility(View.VISIBLE);
            binding.readBookBtn.setVisibility(View.GONE);
        }
    }

    private void openBookChapter(){
        purchasedBookApi.getPurchasedBook(bookId).enqueue(new RetrofitCallBack<GetPurchasedBookResponse>() {
            @Override
            public void onSuccess(GetPurchasedBookResponse response) {
                if (response != null){
                    if (response.getCode() == 106){
                        Toast.makeText(ViewBookActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    } else if (response.getCode() == 112) {
                        Toast.makeText(ViewBookActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    } else if (response.getCode() == 100) {
                        PurchasedBook purchasedBook = response.getData();
                        chapterNumber = purchasedBook.getLastReadChapter();
                        Intent intent = new Intent(ViewBookActivity.this, ViewChapterActivity.class);
                        intent.putExtra("bookId", bookId);
                        intent.putExtra("chapterNumber", chapterNumber);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private String toStringCategory(List<String> categoriesName){
        String show = "";
        int size = categoriesName.size();
        if (size == 0){
            return show;
        } else if (size == 1) {
            return categoriesName.get(0);
        } else {
            show = categoriesName.get(0);
            for (int i = 1; i < size; i++){
                show += (", "+ categoriesName.get(i));
            }
            return show;
        }
    }

}