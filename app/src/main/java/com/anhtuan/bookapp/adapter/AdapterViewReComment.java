package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.databinding.RowReCommentBinding;
import com.anhtuan.bookapp.domain.ReComment;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.bumptech.glide.Glide;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterViewReComment extends RecyclerView.Adapter<AdapterViewReComment.HolderViewReComment>{

    private Context context;
    List<ReComment> reCommentList;
    RowReCommentBinding binding;
    User user;

    public AdapterViewReComment(Context context, List<ReComment> reCommentList) {
        this.context = context;
        this.reCommentList = reCommentList;
    }

    @NonNull
    @Override
    public HolderViewReComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowReCommentBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderViewReComment(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderViewReComment holder, int position) {
        ReComment reComment = reCommentList.get(position);
        String author = reComment.getAuthor();
        String commentContent = reComment.getCommentContent();
        long commentTime = reComment.getCommentTime();
        long time = System.currentTimeMillis();

        holder.bodyCmt.setText(commentContent);
        holder.timeCmt.setText(Utils.covertLongToTimeString(time - commentTime));

        if (author != null){
            userApi.getUserInfo(author).enqueue(new Callback<GetUserInfoResponse>() {
                @Override
                public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                    if (response.body() != null){
                        if (response.body().getCode() == 100){
                            user = response.body().getData();
                            holder.nameTv.setText(user.getName());
                            String imageName = user.getAvatarImage();
                            Boolean isLoginGoogle = user.getGoogleLogin();

                            if (!Objects.isNull(isLoginGoogle) && isLoginGoogle){
                                Glide.with(context)
                                        .load(imageName)
                                        .into(holder.avatar);

                            }

                            if (imageName != null && Objects.isNull(isLoginGoogle)){
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
    }

    @Override
    public int getItemCount() {
        return reCommentList.size();
    }

    class HolderViewReComment extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView nameTv, bodyCmt, timeCmt;

        public HolderViewReComment(@NonNull View itemView) {
            super(itemView);
            avatar = binding.avatar;
            nameTv = binding.nameTv;
            bodyCmt = binding.bodyCmt;
            timeCmt = binding.timeCmt;
        }
    }

}
