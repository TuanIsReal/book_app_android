package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anhtuan.bookapp.activity.ViewReCommentActivity;
import com.anhtuan.bookapp.databinding.RowCommentBinding;
import com.anhtuan.bookapp.domain.Comment;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.bumptech.glide.Glide;
import java.io.IOException;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterViewComment extends RecyclerView.Adapter<AdapterViewComment.HolderViewComment>{
    private Context context;
    List<Comment> commentList;
    RowCommentBinding binding;
    User user;


    public AdapterViewComment(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public HolderViewComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowCommentBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderViewComment(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderViewComment holder, int position) {
        Comment comment = commentList.get(position);
        String bookId = comment.getBookId();
        String author = comment.getAuthor();
        String commentContent = comment.getCommentContent();
        int totalReComment = comment.getTotalReComment();
        long commentTime = comment.getCommentTime();
        long time = System.currentTimeMillis();

        holder.bodyCmt.setText(commentContent);
        holder.numberReCmt.setText(String.valueOf(totalReComment));
        holder.timeCmt.setText(covertLongToTimeString(time - commentTime));

        if (author != null){
            userApi.getUserInfo(author).enqueue(new Callback<GetUserInfoResponse>() {
                @Override
                public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                    if (response.body() != null){
                        if (response.body().getCode() == 100){
                            user = response.body().getData();
                            holder.nameTv.setText(user.getName());

                            if (user.getAvatarImage() != null){
                                userApi.getAvatarImage(user.getAvatarImage()).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()){
                                            try {
                                                byte[] bytes = response.body().bytes();
                                                Glide.with(context)
                                                        .load(bytes)
                                                        .into(holder.avatar);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.d("err", "err--fail");
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetUserInfoResponse> call, Throwable t) {
                    Toast.makeText(context, "" + t, Toast.LENGTH_SHORT).show();
                }
            });
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewReCommentActivity.class);
                intent.putExtra("parentComment", comment);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    class HolderViewComment extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView nameTv, bodyCmt, timeCmt, numberReCmt;

        public HolderViewComment(@NonNull View itemView) {
            super(itemView);
            avatar = binding.avatar;
            nameTv = binding.nameTv;
            bodyCmt = binding.bodyCmt;
            timeCmt = binding.timeCmt;
            numberReCmt = binding.numberReCmt;
        }
    }

    private String covertLongToTimeString(long time){
        long number;
        if (time < 3600000){
            number = Math.floorDiv(time, 60000);
            return number + " phút trước";
        }

        if (time < 43200000){
            number = Math.floorDiv(time, 3600000);
            return number + " giờ trước";
        }

        number = Math.floorDiv(time, 43200000);
        return number + " ngày trước";
    }
}
