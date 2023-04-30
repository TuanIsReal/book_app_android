package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.CommentApi.commentApi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterViewComment;
import com.anhtuan.bookapp.databinding.ActivityViewCommentBinding;
import com.anhtuan.bookapp.domain.Comment;
import com.anhtuan.bookapp.request.AddCommentRequest;
import com.anhtuan.bookapp.response.GetCommentListResponse;
import com.anhtuan.bookapp.response.NoDataResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCommentActivity extends AppCompatActivity {

    ActivityViewCommentBinding binding;
    List<Comment> commentList;
    AdapterViewComment adapterViewComment;
    String bookId;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");
        userId = intent.getStringExtra("userId");
        commentList = new ArrayList<>();

        loadCommentList();

        binding.sendCmtIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = binding.cmtContent.getText().toString().trim();
                if (!commentContent.isBlank()){
                    AddCommentRequest request = new AddCommentRequest(bookId, userId, commentContent);
                    commentApi.addComment(request).enqueue(new Callback<NoDataResponse>() {
                        @Override
                        public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                            if (response.body() != null && response.body().getCode() == 100){
                                binding.cmtContent.setText("");
                                loadCommentList();
                            }
                        }

                        @Override
                        public void onFailure(Call<NoDataResponse> call, Throwable t) {
                        }
                    });
                }
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadCommentList() {
        commentApi.getCommentList(bookId).enqueue(new Callback<GetCommentListResponse>() {
            @Override
            public void onResponse(Call<GetCommentListResponse> call, Response<GetCommentListResponse> response) {
                if (response.body() != null){
                    if (response.body().getCode() == 100){
                        commentList = response.body().getData();
                        adapterViewComment = new AdapterViewComment(ViewCommentActivity.this, commentList);
                        binding.cmtList.setAdapter(adapterViewComment);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCommentListResponse> call, Throwable t) {
                Toast.makeText(ViewCommentActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}