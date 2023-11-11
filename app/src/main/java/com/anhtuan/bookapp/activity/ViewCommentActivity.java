package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.CommentApi.commentApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.anhtuan.bookapp.adapter.AdapterViewComment;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.ActivityViewCommentBinding;
import com.anhtuan.bookapp.domain.Comment;
import com.anhtuan.bookapp.request.AddCommentRequest;
import com.anhtuan.bookapp.response.GetCommentListResponse;
import com.anhtuan.bookapp.response.NoDataResponse;

import java.util.ArrayList;
import java.util.List;

public class ViewCommentActivity extends AppCompatActivity {

    ActivityViewCommentBinding binding;
    List<Comment> commentList;
    AdapterViewComment adapterViewComment;
    String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityViewCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");
        commentList = new ArrayList<>();

        loadCommentList();

        binding.sendCmtIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = binding.cmtContent.getText().toString().trim();
                if (!commentContent.isBlank()){
                    AddCommentRequest request = new AddCommentRequest(bookId, commentContent);
                    commentApi.addComment(request).enqueue(new RetrofitCallBack<NoDataResponse>() {
                        @Override
                        public void onSuccess(NoDataResponse response) {
                            if (response != null && response.getCode() == 100){
                                binding.cmtContent.setText("");
                                loadCommentList();
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {

                        }
                    });
                }
            }
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCommentList();
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
        commentApi.getCommentList(bookId).enqueue(new RetrofitCallBack<GetCommentListResponse>() {
            @Override
            public void onSuccess(GetCommentListResponse response) {
                binding.swipeRefresh.setRefreshing(false);
                if (response != null){
                    if (response.getCode() == 100){
                        commentList = response.getData();
                        adapterViewComment = new AdapterViewComment(ViewCommentActivity.this, commentList);
                        binding.cmtList.setAdapter(adapterViewComment);
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }
}