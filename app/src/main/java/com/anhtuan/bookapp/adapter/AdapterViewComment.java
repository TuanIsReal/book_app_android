package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anhtuan.bookapp.activity.ViewReCommentActivity;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.RowCommentBinding;
import com.anhtuan.bookapp.domain.Comment;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.response.ImageResponse;
import com.bumptech.glide.Glide;
import java.util.List;
import java.util.Objects;
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
        holder.timeCmt.setText(Utils.covertLongToTimeString(time - commentTime));

        if (author != null){
            userApi.getUserInfo(author).enqueue(new RetrofitCallBack<GetUserInfoResponse>() {
                @Override
                public void onSuccess(GetUserInfoResponse response) {
                    if (response != null){
                        if (response.getCode() == 100){
                            user = response.getData();
                            holder.nameTv.setText(user.getName());
                            String imageName = user.getAvatarImage();
                            Boolean isLoginGoogle = user.getGoogleLogin();

                            if (!Objects.isNull(isLoginGoogle) && isLoginGoogle){
                                Glide.with(context)
                                        .load(imageName)
                                        .into(holder.avatar);

                            }

                            if (imageName != null && Objects.isNull(isLoginGoogle)){
                                unAuthApi.getThumbnail(user.getAvatarImage()).enqueue(new Callback<ImageResponse>() {
                                    @Override
                                    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                                        if (response.body().getCode() == 100){
                                            String path = Constant.IP_SERVER_IMAGE + response.body().getData();
                                            Glide.with(context)
                                                    .load(path)
                                                    .into(holder.avatar);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ImageResponse> call, Throwable t) {
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
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

}
