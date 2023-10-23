package com.anhtuan.bookapp.activity;


import static com.anhtuan.bookapp.api.CategoryApi.categoryApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.anhtuan.bookapp.api.CategoryApi;
import com.anhtuan.bookapp.databinding.ActivityCategoryAddBinding;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        categoryApi.addCategory(category).enqueue(new Callback<NoDataResponse>() {
            @Override
            public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                NoDataResponse addCategoryResponse = response.body();
                if (addCategoryResponse.getCode() == 100){
                    binding.categoryEt.setText("");
                    progressDialog.dismiss();
                    Toast.makeText(CategoryAddActivity.this, "Thêm loại sách thành công", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(CategoryAddActivity.this, "Tên loại sách đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NoDataResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CategoryAddActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}