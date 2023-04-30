package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.BookChapterApi.bookChapterApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.anhtuan.bookapp.databinding.ActivityBookChapterAddBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.request.AddChapterRequest;
import com.anhtuan.bookapp.response.GetBookResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookChapterAddActivity extends AppCompatActivity {

    private ActivityBookChapterAddBinding binding;
    private List<Book> books;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookChapterAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");
        progressDialog = new ProgressDialog(this);
        loadBooks(userId);
        Intent intent = getIntent();
        String bookName = intent.getStringExtra("bookName");
        if (bookName != null){
            binding.bookTv.setText(bookName);
        }

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.bookTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookPickDialog();
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookName = binding.bookTv.getText().toString().trim();
                String chapterNumberString = binding.chapterNumberEt.getText().toString();
                String chapterName = binding.chapterNameEt.getText().toString().trim();
                String chapterContent = binding.chapterContentEt.getText().toString().trim();

                if (validateData(bookName, chapterNumberString, chapterName, chapterContent)){
                    int chapterNumber = Integer.parseInt(chapterNumberString);
                    progressDialog.setTitle("");
                    progressDialog.setMessage("Đang thêm chapter...");
                    progressDialog.show();
                    bookChapterApi.addChapter(new AddChapterRequest(bookName, chapterNumber, chapterName, chapterContent))
                            .enqueue(new Callback<NoDataResponse>() {
                        @Override
                        public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                            progressDialog.dismiss();
                            NoDataResponse responseBody = response.body();
                            if (responseBody == null){
                                Toast.makeText(BookChapterAddActivity.this, "Call Api lỗi", Toast.LENGTH_SHORT).show();
                            } else if (responseBody.getCode() == 109){
                                Toast.makeText(BookChapterAddActivity.this, "Không tìm thấy sách được chọn", Toast.LENGTH_SHORT).show();
                            } else if (responseBody.getCode() == 107) {
                                Toast.makeText(BookChapterAddActivity.this, "Số chương bị trùng", Toast.LENGTH_SHORT).show();
                            } else if (responseBody.getCode() == 108) {
                                Toast.makeText(BookChapterAddActivity.this, "data truyền bị lỗi", Toast.LENGTH_SHORT).show();
                            }  else if (responseBody.getCode() == 100) {
                                Toast.makeText(BookChapterAddActivity.this, "Thêm chương thành công", Toast.LENGTH_SHORT).show();
                                binding.chapterNumberEt.setText("");
                                binding.chapterNameEt.setText("");
                                binding.chapterContentEt.setText("");
                            } else {
                                Toast.makeText(BookChapterAddActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<NoDataResponse> call, Throwable t) {
                            Toast.makeText(BookChapterAddActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void loadBooks(String userId) {
        if (userId == null){
            Toast.makeText(BookChapterAddActivity.this, "UserId null", Toast.LENGTH_SHORT).show();
        } else {
            books = new ArrayList<>();
            bookApi.getBookByUserId(userId).enqueue(new Callback<GetBookResponse>() {
                @Override
                public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                    GetBookResponse responseBody = response.body();
                    if (responseBody.getCode() == 106){
                        Toast.makeText(BookChapterAddActivity.this, "Không tìm thấy user", Toast.LENGTH_SHORT).show();
                    }
                    if (responseBody.getCode() == 100){
                        books = responseBody.getData();
                    }
                }

                @Override
                public void onFailure(Call<GetBookResponse> call, Throwable t) {
                    Toast.makeText(BookChapterAddActivity.this, ""+ t, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private void bookPickDialog(){
        String[] booksName = new String[books.size()];
        for (int i = 0; i < books.size(); i++){
            booksName[i] = books.get(i).getBookName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn sách")
                .setItems(booksName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String book = booksName[which];
                        binding.bookTv.setText(book);
                    }
                }).show();
    }


    private boolean validateData(String bookName, String chapterNumberString, String chapterName, String chapterContent){
        if (bookName.isBlank()){
            Toast.makeText(BookChapterAddActivity.this, "Chưa chọn sách", Toast.LENGTH_SHORT).show();
            return false;
        } else if (chapterNumberString.isBlank()) {
            Toast.makeText(BookChapterAddActivity.this, "Chưa nhập số chương", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(chapterNumberString) <= 0) {
            Toast.makeText(BookChapterAddActivity.this, "Số chương phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return false;
        } else if (chapterName.isBlank()) {
            Toast.makeText(BookChapterAddActivity.this, "Chưa nhập tên chương", Toast.LENGTH_SHORT).show();
            return false;
        } else if (chapterContent.isBlank()) {
            Toast.makeText(BookChapterAddActivity.this, "Chưa nhập nội dung chương", Toast.LENGTH_SHORT).show();
            return false;
        }
        String[] wordList = chapterContent.split("\\s");
        if (wordList.length < 100 || wordList.length > 10000){
            Toast.makeText(BookChapterAddActivity.this, "Nội dung chương ít nhất 100 từ và nhiều nhất 10000 từ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}