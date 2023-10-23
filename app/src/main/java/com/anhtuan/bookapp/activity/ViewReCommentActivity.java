package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.ReCommentApi.reCommentApi;
import static com.anhtuan.bookapp.api.STFApi.stfApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.anhtuan.bookapp.adapter.AdapterViewReComment;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.databinding.ActivityViewReCommentBinding;
import com.anhtuan.bookapp.domain.Comment;
import com.anhtuan.bookapp.domain.ReComment;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.request.AddReCommentRequest;
import com.anhtuan.bookapp.response.GetReCommentListResponse;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.response.ImageResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.bumptech.glide.Glide;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewReCommentActivity extends AppCompatActivity {
    ActivityViewReCommentBinding binding;
    AdapterViewReComment adapterViewReComment;
    List<ReComment> reCommentList;
    User user;
    Comment parentComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityViewReCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");

        reCommentList = new ArrayList<>();
        Intent intent = getIntent();
        parentComment = (Comment) intent.getSerializableExtra("parentComment");

        if (parentComment != null){
            setParentCommentInfo();
        }

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.sendCmtIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = binding.cmtContent.getText().toString().trim();
                if (!commentContent.isBlank()){
                    AddReCommentRequest request = new AddReCommentRequest(parentComment.getId(), userId, commentContent);
                    reCommentApi.addReComment(request).enqueue(new Callback<NoDataResponse>() {
                        @Override
                        public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                            if (response.body() != null && response.body().getCode() == 100){
                                binding.cmtContent.setText("");
                                loadReCommentList();
                            }
                        }

                        @Override
                        public void onFailure(Call<NoDataResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void setParentCommentInfo(){
        binding.bodyMainCmt.setText(parentComment.getCommentContent());
        binding.timeMainCmt.setText(Utils.covertLongToTimeString(System.currentTimeMillis() - parentComment.getCommentTime()));

        userApi.getUserInfo(parentComment.getAuthor()).enqueue(new Callback<GetUserInfoResponse>() {
            @Override
            public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                if (response.body() != null){
                    if (response.body().getCode() == 100){
                        user = response.body().getData();
                        binding.nameTv.setText(user.getName());
                        if (user.getAvatarImage() != null && user.getGoogleLogin()){
                            Glide.with(ViewReCommentActivity.this)
                                    .load(user.getAvatarImage())
                                    .into(binding.avatar);
                            loadReCommentList();
                        } else if (user.getAvatarImage() != null){
                            stfApi.getThumbnail(user.getAvatarImage()).enqueue(new Callback<ImageResponse>() {
                                @Override
                                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                                    if (response.body().getCode() == 100){
                                        Glide.with(ViewReCommentActivity.this)
                                                .load(response.body().getData())
                                                .into(binding.avatar);
                                        loadReCommentList();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ImageResponse> call, Throwable t) {
                                    Log.d("err", "err--fail");
                                }
                            });
                        } else {
                            loadReCommentList();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserInfoResponse> call, Throwable t) {
                Toast.makeText(ViewReCommentActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadReCommentList(){
        reCommentApi.getReCommentList(parentComment.getId()).enqueue(new Callback<GetReCommentListResponse>() {
            @Override
            public void onResponse(Call<GetReCommentListResponse> call, Response<GetReCommentListResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    reCommentList = response.body().getData();
                    if (reCommentList.size() > 0){
                        binding.blankTv.setVisibility(View.GONE);
                    }
                    adapterViewReComment = new AdapterViewReComment(ViewReCommentActivity.this, reCommentList);
                    binding.reCmtList.setAdapter(adapterViewReComment);
                }
            }

            @Override
            public void onFailure(Call<GetReCommentListResponse> call, Throwable t) {
                Toast.makeText(ViewReCommentActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }


}