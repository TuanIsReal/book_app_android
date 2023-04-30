package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.BookApi.bookApi;

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
import com.anhtuan.bookapp.databinding.ColumnMostBuyBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMostBuyBook extends RecyclerView.Adapter<AdapterMostBuyBook.HolderMostBuyBook> {
    private Context context;
    public List<Book> bookList;
    public ColumnMostBuyBookBinding binding;


    public AdapterMostBuyBook(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }


    @NonNull
    @Override
    public HolderMostBuyBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ColumnMostBuyBookBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderMostBuyBook(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMostBuyBook holder, int position) {
        Book book = bookList.get(position);
        String bookImage = book.getBookImage();
        holder.bookName.setText(book.getBookName());
        holder.purchaseNumber.setText(String.valueOf(book.getTotalPurchased()));

        if (bookImage != null && !bookImage.isBlank()){
            bookApi.getBookImage(bookImage).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        try {
                            byte[] bytes = response.body().bytes();
                            Glide.with(context)
                                    .load(bytes)
                                    .into(holder.bookImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

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


    class HolderMostBuyBook extends RecyclerView.ViewHolder{
        ImageView bookImage;
        TextView bookName, purchaseNumber;
        public HolderMostBuyBook(@NonNull View itemView) {
            super(itemView);
            bookImage = binding.bookImage;
            bookName = binding.bookName;
            purchaseNumber = binding.purchaseNumber;
        }

    }

}
