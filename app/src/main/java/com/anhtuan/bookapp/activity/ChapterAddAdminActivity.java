package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookChapterApi.bookChapterApi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.databinding.ActivityBookChapterAddBinding;
import com.anhtuan.bookapp.databinding.ActivityChapterAddAdminBinding;
import com.anhtuan.bookapp.request.AddChapterRequest;
import com.anhtuan.bookapp.response.NoDataResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterAddAdminActivity extends AppCompatActivity {

    ActivityChapterAddAdminBinding binding;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChapterAddAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        String bookName = intent.getStringExtra("bookName");
        if (bookName != null){
            binding.bookTv.setText(bookName);
        }

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChapterAddAdminActivity.this, ManageBookActivity.class));
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
                                        Toast.makeText(ChapterAddAdminActivity.this, "Call Api lỗi", Toast.LENGTH_SHORT).show();
                                    } else if (responseBody.getCode() == 109){
                                        Toast.makeText(ChapterAddAdminActivity.this, "Không tìm thấy sách được chọn", Toast.LENGTH_SHORT).show();
                                    } else if (responseBody.getCode() == 107) {
                                        Toast.makeText(ChapterAddAdminActivity.this, "Số chương bị trùng", Toast.LENGTH_SHORT).show();
                                    } else if (responseBody.getCode() == 108) {
                                        Toast.makeText(ChapterAddAdminActivity.this, "data truyền bị lỗi", Toast.LENGTH_SHORT).show();
                                    }  else if (responseBody.getCode() == 100) {
                                        Toast.makeText(ChapterAddAdminActivity.this, "Thêm chương thành công", Toast.LENGTH_SHORT).show();
                                        binding.chapterNumberEt.setText("");
                                        binding.chapterNameEt.setText("");
                                        binding.chapterContentEt.setText("");
                                    } else {
                                        Toast.makeText(ChapterAddAdminActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<NoDataResponse> call, Throwable t) {
                                    Toast.makeText(ChapterAddAdminActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    private boolean validateData(String bookName, String chapterNumberString, String chapterName, String chapterContent){
        if (bookName.isBlank()){
            Toast.makeText(ChapterAddAdminActivity.this, "Chưa chọn sách", Toast.LENGTH_SHORT).show();
            return false;
        } else if (chapterNumberString.isBlank()) {
            Toast.makeText(ChapterAddAdminActivity.this, "Chưa nhập số chương", Toast.LENGTH_SHORT).show();
            return false;
        } else if (chapterName.isBlank()) {
            Toast.makeText(ChapterAddAdminActivity.this, "Chưa nhập tên chương", Toast.LENGTH_SHORT).show();
            return false;
        } else if (chapterContent.isBlank()) {
            Toast.makeText(ChapterAddAdminActivity.this, "Chưa nhập nội dung chương", Toast.LENGTH_SHORT).show();
            return false;
        }
        String[] wordList = chapterContent.split("\\s");
        if (wordList.length < 100 || wordList.length > 10000){
            Toast.makeText(ChapterAddAdminActivity.this, "Nội dung chương ít nhất 100 từ và nhiều nhất 1000 từ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}