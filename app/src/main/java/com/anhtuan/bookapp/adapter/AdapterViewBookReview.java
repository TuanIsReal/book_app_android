package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.RowBookReviewBinding;
import com.anhtuan.bookapp.domain.BookReview;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.response.ImageResponse;
import com.bumptech.glide.Glide;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterViewBookReview extends RecyclerView.Adapter<AdapterViewBookReview.HolderViewBookReview>{
    private Context context;
    private List<BookReview> bookReviewList;
    RowBookReviewBinding binding;


    public AdapterViewBookReview(Context context, List<BookReview> bookReviewList) {
        this.context = context;
        this.bookReviewList = bookReviewList;
    }

    @NonNull
    @Override
    public HolderViewBookReview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowBookReviewBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderViewBookReview(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderViewBookReview holder, int position) {
        BookReview bookReview = bookReviewList.get(position);
        String author = bookReview.getAuthor();
        double reviewStar = bookReview.getReviewStar();
        String reviewContent = bookReview.getReviewContent();
        long reviewTime = bookReview.getReviewTime();

        holder.ratingBar.setRating((float) reviewStar);
        holder.reviewContent.setText(reviewContent);
        holder.timeReview.setText(Utils.covertLongToTimeString(System.currentTimeMillis() - reviewTime));
        userApi.getUserInfo(author).enqueue(new RetrofitCallBack<GetUserInfoResponse>() {
            @Override
            public void onSuccess(GetUserInfoResponse response) {
                if (response != null && response.getCode() == 100){
                    User user = response.getData();
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

    @Override
    public int getItemCount() {
        return bookReviewList.size();
    }


    class HolderViewBookReview extends RecyclerView.ViewHolder{
        TextView nameTv, reviewContent, timeReview;
        CircleImageView avatar;
        RatingBar ratingBar;

        public HolderViewBookReview(@NonNull View itemView) {
            super(itemView);
            nameTv = binding.nameTv;
            reviewContent = binding.reviewContent;
            timeReview = binding.timeReview;
            avatar = binding.avatar;
            ratingBar = binding.ratingBar;
        }
    }
}
