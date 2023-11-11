package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.RowReCommentBinding;
import com.anhtuan.bookapp.domain.ReComment;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.response.ImageResponse;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

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
            userApi.getUserInfo(author).enqueue(new RetrofitCallBack<GetUserInfoResponse>() {
                @Override
                public void onSuccess(GetUserInfoResponse response) {
                    if (response != null && response.getCode() == 100){
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

                @Override
                public void onFailure(String errorMessage) {

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
