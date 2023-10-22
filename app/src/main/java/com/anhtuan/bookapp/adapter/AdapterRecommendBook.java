package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.STFApi.stfApi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.activity.ViewBookActivity;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.databinding.ColumnRecommendBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.ImageResponse;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterRecommendBook extends RecyclerView.Adapter<AdapterRecommendBook.HolderRecommendBook> {
    private Context context;
    public List<Book> bookList;
    public ColumnRecommendBookBinding binding;


    public AdapterRecommendBook(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }


    @NonNull
    @Override
    public HolderRecommendBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ColumnRecommendBookBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderRecommendBook(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecommendBook holder, int position) {
        Book book = bookList.get(position);
        String bookImage = book.getBookImage();
        holder.recommendNameTv.setText(book.getBookName());
        holder.recommendCategoryTv.setText(Utils.toStringCategory(book.getBookCategory()));

        if (bookImage != null && !bookImage.isBlank()){
            stfApi.getThumbnail(bookImage).enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                    if (response.body().getCode() == 100){
                        Glide.with(context)
                                .load(response.body().getData())
                                .into(holder.recommendImageIv);
                    }
                }

                @Override
                public void onFailure(Call<ImageResponse> call, Throwable t) {

                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewBookActivity.class);
                intent.putExtra("bookId", book.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }


    class HolderRecommendBook extends RecyclerView.ViewHolder{
        ImageView recommendImageIv;
        TextView recommendNameTv, recommendCategoryTv;
        public HolderRecommendBook(@NonNull View itemView) {
            super(itemView);
            recommendImageIv = binding.recommendImageIv;
            recommendNameTv = binding.recommendNameTv;
            recommendCategoryTv = binding.recommendCategoryTv;
        }

    }

}
