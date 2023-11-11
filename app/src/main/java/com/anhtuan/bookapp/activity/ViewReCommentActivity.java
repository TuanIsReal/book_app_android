package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.ReCommentApi.reCommentApi;
import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.anhtuan.bookapp.adapter.AdapterViewReComment;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.config.Constant;
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
import java.util.ArrayList;
import java.util.List;
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
                    AddReCommentRequest request = new AddReCommentRequest(parentComment.getId(), commentContent);
                    reCommentApi.addReComment(request).enqueue(new RetrofitCallBack<NoDataResponse>() {
                        @Override
                        public void onSuccess(NoDataResponse response) {
                            if (response != null && response.getCode() == 100){
                                binding.cmtContent.setText("");
                                loadReCommentList();
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {

                        }
                    });
                }
            }
        });
    }

    private void setParentCommentInfo(){
        binding.bodyMainCmt.setText(parentComment.getCommentContent());
        binding.timeMainCmt.setText(Utils.covertLongToTimeString(System.currentTimeMillis() - parentComment.getCommentTime()));

        userApi.getUserInfo(parentComment.getAuthor()).enqueue(new RetrofitCallBack<GetUserInfoResponse>() {
            @Override
            public void onSuccess(GetUserInfoResponse response) {
                if (response != null){
                    if (response.getCode() == 100){
                        user = response.getData();
                        binding.nameTv.setText(user.getName());
                        if (user.getAvatarImage() != null && user.getGoogleLogin() != null && user.getGoogleLogin()){
                            Glide.with(ViewReCommentActivity.this)
                                    .load(user.getAvatarImage())
                                    .into(binding.avatar);
                            loadReCommentList();
                        } else if (user.getAvatarImage() != null){
                            unAuthApi.getThumbnail(user.getAvatarImage()).enqueue(new Callback<ImageResponse>() {
                                @Override
                                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                                    if (response.body().getCode() == 100){
                                        String path = Constant.IP_SERVER_IMAGE + response.body().getData();
                                        Glide.with(ViewReCommentActivity.this)
                                                .load(path)
                                                .into(binding.avatar);
                                        loadReCommentList();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ImageResponse> call, Throwable t) {
                                }
                            });
                        } else {
                            loadReCommentList();
                        }
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    private void loadReCommentList(){
        reCommentApi.getReCommentList(parentComment.getId()).enqueue(new RetrofitCallBack<GetReCommentListResponse>() {
            @Override
            public void onSuccess(GetReCommentListResponse response) {
                if (response != null && response.getCode() == 100){
                    reCommentList = response.getData();
                    if (reCommentList.size() > 0){
                        binding.blankTv.setVisibility(View.GONE);
                    }
                    adapterViewReComment = new AdapterViewReComment(ViewReCommentActivity.this, reCommentList);
                    binding.reCmtList.setAdapter(adapterViewReComment);
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }


}