package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.AccountManager;
import com.anhtuan.bookapp.common.ApiAddress;
import com.anhtuan.bookapp.common.RealPathUtil;
import com.anhtuan.bookapp.common.TokenManager;
import com.anhtuan.bookapp.databinding.ActivityRegisterBinding;
import com.anhtuan.bookapp.request.RegisterRequest;
import com.anhtuan.bookapp.response.RegisterData;
import com.anhtuan.bookapp.response.RegisterResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private ProgressDialog progressDialog;
    private static final int MY_REQUEST_CODE = 10;
    private static final int IMAGE_PICK_CODE = 999;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Xin chờ");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private String name, email, password, confirmPassword;

    private void validateData(){
        name = binding.nameEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();
        confirmPassword = binding.confirmPasswordEt.getText().toString().trim();

        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()){
            Toast.makeText(this,"Không được bỏ trống!!!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this,"Email sai định dạng!!!", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this,"Mật khẩu không được nhỏ hơn 6 ký tự!!!", Toast.LENGTH_SHORT).show();
        } else if (!confirmPassword.equals(password)) {
            Toast.makeText(this,"Mật khẩu nhập lại không khớp!!!", Toast.LENGTH_SHORT).show();
        } else {
            creatAccountUser();
        }
    }

    private void creatAccountUser(){
        progressDialog.setMessage("Đang kiểm tra tài khoản...");
        progressDialog.show();

        unAuthApi.checkExistUser(email).enqueue(new Callback<NoDataResponse>() {
            @Override
            public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                NoDataResponse responseCheck = response.body();
                if (responseCheck.getCode() == 101){
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveUser();
                }
            }

            @Override
            public void onFailure(Call<NoDataResponse> call, Throwable t) {

            }
        });



    }

    private void saveUser(){
        progressDialog.setMessage("Đang đăng ký tài khoản...");
        progressDialog.show();

        unAuthApi.register(new RegisterRequest(email, password, name, new ApiAddress().getIPAddress())).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse registerResponse = response.body();
                if (registerResponse.getCode() != 100) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                } else {
                    RegisterData registerData = registerResponse.getData();
                    AccountManager.getInstance().saveAccount(email, "");
                    TokenManager.getInstance().saveToken(registerData.getToken(), registerData.getRefreshToken());
                    if (imageUri != null){
                        updateAvatarImage(registerData.getRole());
                    } else {
                        sendDeviceToken(registerData.getRole());
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+ t, Toast.LENGTH_LONG).show();
            }
        });
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
            Toast.makeText(RegisterActivity.this, "Hủy chọn ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAvatarImage(int role){
        try {
            String realImagePath = RealPathUtil.copyFileToInternal(RegisterActivity.this, imageUri);
            Glide.with(this)
                    .asBitmap()
                    .load(realImagePath)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                            RequestBody image = RequestBody.create(MediaType.parse("multipart/form-data"), stream.toByteArray());
                            MultipartBody.Part multipartBodyImage = MultipartBody.Part.createFormData("image", "", image);
                            unAuthApi.updateAvatarImage(multipartBodyImage).enqueue(new Callback<NoDataResponse>() {
                                @Override
                                public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                                    progressDialog.dismiss();
                                    NoDataResponse responseBody = response.body();
                                    if (responseBody == null){
                                        Toast.makeText(RegisterActivity.this, "Call Api lỗi", Toast.LENGTH_SHORT).show();
                                    } else if (responseBody.getCode() == 106){
                                        Toast.makeText(RegisterActivity.this, "Không tìm user được chọn", Toast.LENGTH_SHORT).show();
                                    } else if (responseBody.getCode() == 108) {
                                        Toast.makeText(RegisterActivity.this, "File ảnh load lên server lỗi", Toast.LENGTH_SHORT).show();
                                    }  else if (responseBody.getCode() == 100) {
                                        sendDeviceToken(role);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Lỗi không xác định", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<NoDataResponse> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Lỗi call API:"+t, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        } catch (Exception e){
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "Lỗi try:"+e, Toast.LENGTH_SHORT).show();
        }

    }

    private void sendDeviceToken(int role){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String token = task.getResult();
                userApi.loginDevice(token).enqueue(new RetrofitCallBack<NoDataResponse>() {
                    @Override
                    public void onSuccess(NoDataResponse response) {
                        if (response != null){
                            if (response.getCode() == 100) {
                                if (role == 2){
                                    startActivity(new Intent(RegisterActivity.this, DashboardAdminActivity.class));
                                    finish();
                                }
                                else {
                                    startActivity(new Intent(RegisterActivity.this, DashboardUserActivity.class));
                                    finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        if (role == 2){
                            startActivity(new Intent(RegisterActivity.this, DashboardAdminActivity.class));
                            finish();
                        }
                        else {
                            startActivity(new Intent(RegisterActivity.this, DashboardUserActivity.class));
                            finish();
                        }
                    }
                });
            }
        });
    }

}