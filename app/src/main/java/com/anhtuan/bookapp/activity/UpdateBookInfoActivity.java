package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.CategoryApi.categoryApi;
import static com.anhtuan.bookapp.api.STFApi.stfApi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.common.RealPathUtil;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.ActivityUpdateBookInfoBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.domain.Category;
import com.anhtuan.bookapp.fragment.UserUploadBookFragment;
import com.anhtuan.bookapp.request.AddBookRequest;
import com.anhtuan.bookapp.request.UpdateBookRequest;
import com.anhtuan.bookapp.response.CategoriesResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.anhtuan.bookapp.response.ViewBookResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateBookInfoActivity extends AppCompatActivity {

    ActivityUpdateBookInfoBinding binding;

    private static final int MY_REQUEST_CODE = 10;
    private static final int IMAGE_PICK_CODE = 998;
    private Set<String> pickedCategoriesSet;
    private ArrayList<Category> categories;
    String bookId;
    private ProgressDialog progressDialog;

    private Uri imageUri;
    String bookName;
    String introduction;
    String bookPriceString;
    String freeChapterString;
    boolean isFinish = false;
    String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityUpdateBookInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        bookId = intent.getStringExtra("uploadBookId");
        progressDialog = new ProgressDialog(UpdateBookInfoActivity.this);

        pickedCategoriesSet = new HashSet<String>();
        loadBookCategories();

        if (bookId != null) {
            loadBookInfo();
        }

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryPickDialog();
            }
        });

        binding.finishCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isFinish = isChecked;
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
                if (validate(bookName, bookPriceString, freeChapterString)){
                    progressDialog.setTitle("");
                    progressDialog.setMessage("Đang cập nhật truyện...");
                    progressDialog.show();
                    int bookPrice = Integer.parseInt(bookPriceString);
                    int freeChapter = Integer.parseInt(freeChapterString);
                    ArrayList<String> pickCategories = new ArrayList<>(pickedCategoriesSet);
                    UpdateBookRequest updateBookRequest = new UpdateBookRequest(bookId, introduction,
                            pickCategories, bookPrice, freeChapter, isFinish);

                    updateBook(updateBookRequest);
                }
            }
        });
    }

    private void updateBook(UpdateBookRequest updateBookRequest) {
        bookApi.updateBookInfo(updateBookRequest).enqueue(new Callback<NoDataResponse>() {
            @Override
            public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                NoDataResponse responseBody = response.body();
                if (responseBody.getCode() == 109){
                    progressDialog.dismiss();
                    Toast.makeText(UpdateBookInfoActivity.this, "truyện không tồn tại", Toast.LENGTH_SHORT).show();
                } else if (responseBody.getCode() == 100 && imageUri != null) {
                    updateBookImage(bookName);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateBookInfoActivity.this, "Sửa thông tin truyện thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<NoDataResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void categoryPickDialog() {
        if (pickedCategoriesSet.size() > 2){
            Toast.makeText(UpdateBookInfoActivity.this, "Chỉ chọn nhiều nhất 3 loại sách", Toast.LENGTH_LONG).show();
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

            }
        });
    }


    private void loadBookInfo() {
        bookApi.getBookById(bookId).enqueue(new Callback<ViewBookResponse>() {
            @Override
            public void onResponse(Call<ViewBookResponse> call, Response<ViewBookResponse> response) {
                ViewBookResponse responseBody = response.body();
                if (responseBody.getCode() == 109) {
                    Toast.makeText(UpdateBookInfoActivity.this, "Sách không tồn tại", Toast.LENGTH_SHORT).show();
                } else if (responseBody.getCode() == 100) {
                    Book book = responseBody.getData();
                    binding.bookNameEt.setText(book.getBookName());
                    binding.introductionEt.setText(book.getIntroduction());
                    binding.priceEt.setText(String.valueOf(book.getBookPrice()));
                    binding.freeChapterEt.setText(String.valueOf(book.getFreeChapter()));
                    imageName = book.getBookImage();
                }
            }

            @Override
            public void onFailure(Call<ViewBookResponse> call, Throwable t) {

            }
        });
    }

    private void initData(){
        bookName = binding.bookNameEt.getText().toString().trim();
        introduction = binding.introductionEt.getText().toString().trim();
        bookPriceString = binding.priceEt.getText().toString();
        freeChapterString = binding.freeChapterEt.getText().toString();
    }

    private boolean validate(String bookName, String bookPriceString, String freeChapterString){

        if (bookName.isBlank()){
            Toast.makeText(UpdateBookInfoActivity.this, "Không được bỏ trống tên sách", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bookPriceString.isBlank()){
            Toast.makeText(UpdateBookInfoActivity.this, "Không được bỏ trống giá sách", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (freeChapterString.isBlank()){
            Toast.makeText(UpdateBookInfoActivity.this, "Không được bỏ trống số chương đọc thử", Toast.LENGTH_SHORT).show();
            return false;
        }

        int bookPrice = Integer.parseInt(bookPriceString);
        if (bookPrice > 9999){
            Toast.makeText(UpdateBookInfoActivity.this, "Giá sách tối đa là 9999", Toast.LENGTH_SHORT).show();
            return false;
        }

        int freeChapter = Integer.parseInt(freeChapterString);
        if (freeChapter < 3){
            Toast.makeText(UpdateBookInfoActivity.this, "Số chương đọc thử tối thiểu là 3", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pickedCategoriesSet.size() == 0){
            Toast.makeText(UpdateBookInfoActivity.this, "Chọn ít nhất 1 loại sách", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateBookImage(String bookName) {
        try {
            String realImagePath = RealPathUtil.copyFileToInternal(UpdateBookInfoActivity.this, imageUri);

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
                                    if (responseBody == null) {
                                        Toast.makeText(UpdateBookInfoActivity.this, "Call Api lỗi", Toast.LENGTH_SHORT).show();
                                    } else if (responseBody.getCode() == 109) {
                                        Toast.makeText(UpdateBookInfoActivity.this, "Không tìm thấy sách được chọn", Toast.LENGTH_SHORT).show();
                                    } else if (responseBody.getCode() == 108) {
                                        Toast.makeText(UpdateBookInfoActivity.this, "File ảnh load lên server lỗi", Toast.LENGTH_SHORT).show();
                                    } else if (responseBody.getCode() == 100) {
                                        finish();
                                    } else {
                                        Toast.makeText(UpdateBookInfoActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<NoDataResponse> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(UpdateBookInfoActivity.this, "Lỗi call API:" + t, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(UpdateBookInfoActivity.this, "Lỗi try:" + e, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(UpdateBookInfoActivity.this, "Hủy chọn ảnh", Toast.LENGTH_LONG).show();
        }
    }
}