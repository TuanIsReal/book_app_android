package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
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
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.RowUserRankingBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.domain.User;
import com.anhtuan.bookapp.response.GetUserInfoResponse;
import com.anhtuan.bookapp.response.ImageResponse;
import com.anhtuan.bookapp.response.ViewBookResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterRankingUser extends RecyclerView.Adapter<AdapterRankingUser.HolderRankingUser> {

    private Context context;
    private Map<String, Double> rankingData;
    int type;
    RowUserRankingBinding binding;

    public AdapterRankingUser(Context context, Map<String, Double> rankingData, int type) {
        this.context = context;
        this.rankingData = rankingData;
        this.type = type;
    }

    @NonNull
    @Override
    public HolderRankingUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowUserRankingBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderRankingUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRankingUser holder, int position) {
        String key = rankingData.keySet().toArray()[position].toString();
        Double value = rankingData.get(key);
        holder.rankTv.setText(String.valueOf(position + 1));
        holder.bodyTv.setText(String.valueOf(value) + " Point");
        if (type == 1) {
            userApi.getUserInfo(key).enqueue(new RetrofitCallBack<GetUserInfoResponse>() {
                @Override
                public void onSuccess(GetUserInfoResponse response) {
                    if (response.getCode() == 100) {
                        User user = response.getData();
                        holder.nameTv.setText(user.getName());
                        String imageName = user.getAvatarImage();
                        Boolean isLoginGoogle = user.getGoogleLogin();
                        if (imageName != null && isLoginGoogle != null && isLoginGoogle) {
                            Glide.with(context).load(imageName).into(holder.avatar);
                        } else if (imageName != null) {
                            unAuthApi.getThumbnail(imageName).enqueue(new Callback<ImageResponse>() {
                                @Override
                                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                                    if (response.body().getCode() == 100) {
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
        } else {
            bookApi.getBookById(key).enqueue(new RetrofitCallBack<ViewBookResponse>() {
                @Override
                public void onSuccess(ViewBookResponse response) {
                    if (response.getCode() == 100) {
                        Book book = response.getData();
                        holder.nameTv.setText(book.getBookName());
                        String imageName = book.getBookImage();
                        if (imageName != null) {
                            unAuthApi.getThumbnail(imageName).enqueue(new Callback<ImageResponse>() {
                                @Override
                                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                                    if (response.body().getCode() == 100) {
                                        String path = Constant.IP_SERVER_IMAGE + response.body().getData();
                                        Glide.with(context)
                                                .load(path)
                                                .signature(new ObjectKey(path))
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
        return rankingData.size();
    }


    class HolderRankingUser extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView nameTv, bodyTv, rankTv;

        public HolderRankingUser(@NonNull View itemView) {
            super(itemView);
            avatar = binding.avatar;
            nameTv = binding.nameTv;
            bodyTv = binding.bodyTv;
            rankTv = binding.rankTv;
        }
    }
}
