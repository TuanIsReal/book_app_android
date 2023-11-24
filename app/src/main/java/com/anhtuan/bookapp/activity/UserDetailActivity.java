package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.ActivityUserDetailBinding;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.GetUserDetailData;
import com.anhtuan.bookapp.response.GetUserDetailResponse;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

public class UserDetailActivity extends AppCompatActivity {

    ActivityUserDetailBinding binding;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityUserDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        if (userId != null){
            setUserInfo();
        }

        binding.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.blockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userApi.blockUser(userId).enqueue(new RetrofitCallBack<NoDataResponse>() {
                    @Override
                    public void onSuccess(NoDataResponse response) {
                        if (response.getCode() == 100){
                            binding.unBlockBtn.setVisibility(View.VISIBLE);
                            binding.blockBtn.setVisibility(View.GONE);
                            setUserInfo();
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                    }
                });
            }
        });

        binding.unBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userApi.unBlockUser(userId).enqueue(new RetrofitCallBack<NoDataResponse>() {
                    @Override
                    public void onSuccess(NoDataResponse response) {
                        if (response.getCode() == 100){
                            binding.unBlockBtn.setVisibility(View.GONE);
                            binding.blockBtn.setVisibility(View.VISIBLE);
                            setUserInfo();
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                    }
                });
            }
        });

        binding.addPointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pointString = binding.addPointEt.getText().toString();

                if (!pointString.isBlank()){
                    int point = Integer.parseInt(pointString);
                    if (point > 0){
                        userApi.addPointAdmin(userId, point).enqueue(new RetrofitCallBack<NoDataResponse>() {
                            @Override
                            public void onSuccess(NoDataResponse response) {
                                if (response.getCode() == 100){
                                    binding.addPointEt.setText("");
                                    setUserInfo();
                                }
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                            }
                        });
                    } else {
                        Toast.makeText(UserDetailActivity.this, "Số point phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserDetailActivity.this, "Chưa nhập số point", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUserInfo() {

        userApi.getUser(userId).enqueue(new RetrofitCallBack<GetUserInfoResponse>() {
            @Override
            public void onSuccess(GetUserInfoResponse response) {
                if (response.getCode() == 100){
                    User user = response.getData();
                    binding.name.setText(user.getName());
                    binding.userId.setText(user.getId());
                    binding.email.setText(user.getEmail());
                    if (user.getStatus() == 1){
                        binding.status.setText("Hoạt động");
                        binding.unBlockBtn.setVisibility(View.GONE);
                        binding.blockBtn.setVisibility(View.VISIBLE);
                    } else if (user.getStatus() == -1) {
                        binding.status.setText("Đang khóa");
                        binding.unBlockBtn.setVisibility(View.VISIBLE);
                        binding.blockBtn.setVisibility(View.GONE);
                    } else {
                        binding.status.setText("Không hoạt động");
                        binding.unBlockBtn.setVisibility(View.GONE);
                        binding.blockBtn.setVisibility(View.VISIBLE);
                    }
                    binding.totalPoint.setText(String.valueOf(user.getPoint()));
                    if (user.getGoogleLogin() != null && user.getGoogleLogin()){
                        Glide.with(UserDetailActivity.this)
                                .load(user.getAvatarImage())
                                .signature(new ObjectKey(user.getAvatarImage()))
                                .into(binding.avatar);
                    } else {
                        String path = Constant.IP_SERVER_IMAGE + user.getAvatarImage();
                        Glide.with(UserDetailActivity.this)
                                .load(path)
                                .signature(new ObjectKey(path))
                                .into(binding.avatar);
                    }

                    setUserDetailInfo();
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void setUserDetailInfo(){
        userApi.getUserDetail(userId).enqueue(new RetrofitCallBack<GetUserDetailResponse>() {
            @Override
            public void onSuccess(GetUserDetailResponse response) {
                if (response.getCode() == 100){
                    GetUserDetailData userDetail = response.getData();
                    binding.totalBook.setText(String.valueOf(userDetail.getTotalBook()));
                    binding.totalPurchasedBook.setText(String.valueOf(userDetail.getTotalPurchasedBook()));
                    binding.purchasedPoint.setText(String.valueOf(userDetail.getPurchasedPoint()));
                    binding.earnedPoint.setText(String.valueOf(userDetail.getEarnedPoint()));
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}