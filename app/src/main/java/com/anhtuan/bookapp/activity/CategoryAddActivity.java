package com.anhtuan.bookapp.activity;


import static com.anhtuan.bookapp.api.CategoryApi.categoryApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.ActivityCategoryAddBinding;
import com.anhtuan.bookapp.response.NoDataResponse;

public class CategoryAddActivity extends AppCompatActivity {

    private ActivityCategoryAddBinding binding;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Xin chờ");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryAddActivity.this, DashboardAdminActivity.class));
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateData()){
                    Toast.makeText(CategoryAddActivity.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else {
                    addCategory();
                }
            }
        });
    }

    private String category;

    private boolean validateData(){
        category = binding.categoryEt.getText().toString().trim();
        return !category.isBlank();
    }

    private void addCategory() {
        progressDialog.setMessage("Đang thêm loại sách...");
        progressDialog.show();
        categoryApi.addCategory(category).enqueue(new RetrofitCallBack<NoDataResponse>() {
            @Override
            public void onSuccess(NoDataResponse response) {
                progressDialog.dismiss();
                if (response.getCode() == 100){
                    binding.categoryEt.setText("");
                    Toast.makeText(CategoryAddActivity.this, "Thêm loại truyện thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CategoryAddActivity.this, "Tên loại truyện đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                progressDialog.dismiss();
            }
        });

    }
}