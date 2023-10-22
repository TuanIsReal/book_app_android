package com.anhtuan.bookapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.common.RealPathUtil;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.ActivityBookAddBinding;
import com.anhtuan.bookapp.domain.Category;
import com.anhtuan.bookapp.request.AddBookRequest;
import com.anhtuan.bookapp.response.CategoriesResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import static com.anhtuan.bookapp.api.BookRequestUpApi.bookRequestUpApi;
import static com.anhtuan.bookapp.api.CategoryApi.categoryApi;
import static com.anhtuan.bookapp.api.STFApi.stfApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static com.anhtuan.bookapp.api.BookApi.bookApi;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookAddActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 10;
    private static final int IMAGE_PICK_CODE = 999;
    private ActivityBookAddBinding binding;

    private ArrayList<Category> categories;

    private Set<String> pickedCategoriesSet;

    private ProgressDialog progressDialog;

    private Uri imageUri;
    private int addType;
    String bookName;
    String author;
    String introduction;
    String bookPriceString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");
        String userRole = sharedPreferences.getString("userRole","member");

        if (userRole.equals("admin")){
            addType = Constant.AddBookType.ADMIN_ADD;
        } else {
            addType = Constant.AddBookType.MEMBER_ADD;
        }

        progressDialog = new ProgressDialog(BookAddActivity.this);

        pickedCategoriesSet = new HashSet<String>();
        loadBookCategories();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryPickDialog();
            }
        });

        binding.addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
                if (validate(bookName, author, bookPriceString)){
                    progressDialog.setTitle("");
                    progressDialog.setMessage("Đang thêm sách...");
                    progressDialog.show();
                    int bookPrice = Integer.parseInt(bookPriceString);
                    ArrayList<String> pickCategories = new ArrayList<>(pickedCategoriesSet);
                    AddBookRequest addBookRequest = new AddBookRequest(userId, bookName, author,
                            introduction, "", pickCategories, bookPrice);

                    addBook(addBookRequest);
                }
            }
        });


    }

    private void loadBookCategories() {
        categories = new ArrayList<>();
        categoryApi.getCategory().enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                CategoriesResponse categoriesResponse = response.body();
                if (categoriesResponse.getCode() == 100){
                    categories = categoriesResponse.getData();
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Toast.makeText(BookAddActivity.this, ""+ t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void categoryPickDialog(){
        if (pickedCategoriesSet.size() > 2){
            Toast.makeText(BookAddActivity.this, "Chỉ chọn nhiều nhất 3 loại sách", Toast.LENGTH_LONG).show();
        }
        else {
            String[] categoriesName = new String[categories.size()];
            for (int i = 0; i < categories.size(); i++){
                categoriesName[i] = categories.get(i).getCategoryName();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chọn loại sách")
                    .setItems(categoriesName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String category = categoriesName[which];
                            pickedCategoriesSet.add(category);
                            binding.categoryTv.setText(pickedCategoriesSet.toString());
                        }
                    }).show();
        }

    }

    private void onClickRequestPermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openStorage();
        } else {
            String[] permission;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                permission = new String[]{Manifest.permission.READ_MEDIA_IMAGES};
            } else {
                permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            }
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }


    private void openStorage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn 1 file ảnh"), IMAGE_PICK_CODE);
        progressDialog.setTitle("Load ảnh");
        progressDialog.setMessage("Đang load...");
        progressDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.dismiss();
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageUri = data.getData();
            if (imageUri != null){
                binding.statusUploadTv.setText("Chọn ảnh thành công");
            }
        } else {
            progressDialog.dismiss();
            Toast.makeText(BookAddActivity.this, "Hủy chọn ảnh", Toast.LENGTH_LONG).show();
        }
    }

    private void updateBookImage(String bookName){
        try {
            String realImagePath = RealPathUtil.copyFileToInternal(BookAddActivity.this, imageUri);

            Glide.with(this)
                    .asBitmap()
                    .load(realImagePath)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                            RequestBody bookNameRB = RequestBody.create(MediaType.parse("multipart/form-data"), bookName);
                            RequestBody image = RequestBody.create(MediaType.parse("image/jpeg"), stream.toByteArray());
                            MultipartBody.Part multipartBodyImage = MultipartBody.Part.createFormData("image", "", image);
                            stfApi.updateBookImage(bookNameRB, multipartBodyImage).enqueue(new Callback<NoDataResponse>() {
                                @Override
                                public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                                    progressDialog.dismiss();
                                    NoDataResponse responseBody = response.body();
                                    if (responseBody == null){
                                        Toast.makeText(BookAddActivity.this, "Call Api lỗi", Toast.LENGTH_SHORT).show();
                                    } else if (responseBody.getCode() == 109){
                                        Toast.makeText(BookAddActivity.this, "Không tìm thấy sách được chọn", Toast.LENGTH_SHORT).show();
                                    } else if (responseBody.getCode() == 108) {
                                        Toast.makeText(BookAddActivity.this, "File ảnh load lên server lỗi", Toast.LENGTH_SHORT).show();
                                    }  else if (responseBody.getCode() == 100) {
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        Toast.makeText(BookAddActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<NoDataResponse> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(BookAddActivity.this, "Lỗi call API:"+t, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

        } catch (Exception e){
            progressDialog.dismiss();
            Toast.makeText(BookAddActivity.this, "Lỗi try:"+e, Toast.LENGTH_SHORT).show();
        }

    }

    private void updateBookRequestUpImage(String bookName){
        try {
            String realImagePath = RealPathUtil.copyFileToInternal(BookAddActivity.this, imageUri);

            Glide.with(this)
                    .asBitmap()
                    .load(realImagePath)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                            RequestBody bookNameRB = RequestBody.create(MediaType.parse("multipart/form-data"), bookName);
                            RequestBody image = RequestBody.create(MediaType.parse("image/jpeg"), stream.toByteArray());
                            MultipartBody.Part multipartBodyImage = MultipartBody.Part.createFormData("image", "", image);
                            bookRequestUpApi.updateBookRequestUpImage(bookNameRB, multipartBodyImage).enqueue(new Callback<NoDataResponse>() {
                                @Override
                                public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                                    progressDialog.dismiss();
                                    NoDataResponse responseBody = response.body();
                                    if (responseBody == null){
                                        Toast.makeText(BookAddActivity.this, "Call Api lỗi", Toast.LENGTH_SHORT).show();
                                    } else if (responseBody.getCode() == 109){
                                        Toast.makeText(BookAddActivity.this, "Không tìm thấy sách được chọn", Toast.LENGTH_SHORT).show();
                                    } else if (responseBody.getCode() == 108) {
                                        Toast.makeText(BookAddActivity.this, "File ảnh load lên server lỗi", Toast.LENGTH_SHORT).show();
                                    }  else if (responseBody.getCode() == 100) {
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        Toast.makeText(BookAddActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<NoDataResponse> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(BookAddActivity.this, "Lỗi call API:"+t, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

        } catch (Exception e){
            progressDialog.dismiss();
            Toast.makeText(BookAddActivity.this, "Lỗi try:"+e, Toast.LENGTH_SHORT).show();
        }
    }

    private void addBook(AddBookRequest addBookRequest){
        bookApi.addBook(addBookRequest).enqueue(new Callback<NoDataResponse>() {
            @Override
            public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                NoDataResponse responseBody = response.body();
                if (responseBody.getCode() == 105){
                    progressDialog.dismiss();
                    Toast.makeText(BookAddActivity.this, "Tên sách đã tồn tại", Toast.LENGTH_SHORT).show();
                }
                if (responseBody.getCode() == 106){
                    progressDialog.dismiss();
                    Toast.makeText(BookAddActivity.this, "UserId không tồn tại", Toast.LENGTH_SHORT).show();
                }
                if (responseBody.getCode() == 100 && imageUri != null){
                    updateBookImage(bookName);
                }
                if (responseBody.getCode() == 100){
                    progressDialog.dismiss();
                    Toast.makeText(BookAddActivity.this, "Thêm sách thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<NoDataResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(BookAddActivity.this, ""+ t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initData(){
        bookName = binding.bookNameEt.getText().toString().trim();
        author = binding.authorEt.getText().toString().trim();
        introduction = binding.introductionEt.getText().toString().trim();
        bookPriceString = binding.priceEt.getText().toString();
    }

    private boolean validate(String bookName, String author, String bookPriceString){

        if (bookName.isBlank()){
            Toast.makeText(BookAddActivity.this, "Không được bỏ trống tên sách", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (author.isBlank()){
            Toast.makeText(BookAddActivity.this, "Không được bỏ trống tên tác giả", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bookPriceString.isBlank()){
            Toast.makeText(BookAddActivity.this, "Không được bỏ trống trống giá sách", Toast.LENGTH_SHORT).show();
            return false;
        }
        int bookPrice = Integer.parseInt(bookPriceString);
        if (bookPrice > 9999){
            Toast.makeText(BookAddActivity.this, "Giá sách tối đa là 9999", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pickedCategoriesSet.size() == 0){
            Toast.makeText(BookAddActivity.this, "Chọn ít nhất 1 loại sách", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}