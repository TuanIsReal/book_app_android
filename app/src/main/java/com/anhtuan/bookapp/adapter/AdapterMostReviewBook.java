package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.STFApi.stfApi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.activity.ViewBookActivity;
import com.anhtuan.bookapp.databinding.ColumnMostReviewBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.ImageResponse;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMostReviewBook extends RecyclerView.Adapter<AdapterMostReviewBook.HolderMostReviewBook> {
    private Context context;
    public List<Book> bookList;
    public ColumnMostReviewBookBinding binding;


    public AdapterMostReviewBook(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }


    @NonNull
    @Override
    public HolderMostReviewBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ColumnMostReviewBookBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderMostReviewBook(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMostReviewBook holder, int position) {
        Book book = bookList.get(position);
        String bookImage = book.getBookImage();
        holder.bookName.setText(book.getBookName());
        holder.ratingPoint.setText(String.valueOf(book.getStar()));
        holder.ratingBar.setRating((float) book.getStar());

        if (bookImage != null && !bookImage.isBlank()){
            stfApi.getThumbnail(bookImage).enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                    if (response.body().getCode() == 100){
                        Glide.with(context)
                                .load(response.body().getData())
                                .into(holder.bookImage);
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


    class HolderMostReviewBook extends RecyclerView.ViewHolder{
        ImageView bookImage;
        TextView bookName, ratingPoint;
        RatingBar ratingBar;
        public HolderMostReviewBook(@NonNull View itemView) {
            super(itemView);
            bookImage = binding.bookImage;
            bookName = binding.bookName;
            ratingPoint = binding.ratingPoint;
            ratingBar = binding.ratingBar;
        }

    }

}
