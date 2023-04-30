package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.BookApi.bookApi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anhtuan.bookapp.activity.ViewBookActivity;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.databinding.RowBookUserBinding;
import com.anhtuan.bookapp.domain.Book;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdapterSearchBookUser extends RecyclerView.Adapter<AdapterSearchBookUser.HolderSearchBookUser>{

    private Context context;
    private ArrayList<Book> books;

    private RowBookUserBinding binding;


    public AdapterSearchBookUser(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public HolderSearchBookUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowBookUserBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderSearchBookUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSearchBookUser holder, int position) {
        Book book = books.get(position);
        String bookName = book.getBookName();
        String author = book.getAuthor();
        int price = book.getBookPrice();
        double star = book.getStar();
        long time = book.getUploadTime();
        List<String> category = book.getBookCategory();

        //
        holder.bookNameTv.setText(bookName);
        holder.authorTv.setText(author);
        holder.priceTv.setText(Integer.toString(price));
        holder.starTv.setText(Double.toString(star));
        holder.dateTv.setText(Utils.covertLongToTimeString(System.currentTimeMillis() - time));
        holder.categoryTv.setText(Utils.toStringCategory(category));
        String bookImage = book.getBookImage();

        if (bookImage.isBlank()){
            holder.progressBar.setVisibility(View.GONE);
        } else {
            bookApi.getBookImage(bookImage).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    holder.progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        try {
                            byte[] bytes = response.body().bytes();
                            Glide.with(context)
                                    .load(bytes)
                                    .into(holder.imageView);
                        } catch (IOException e) {
                            holder.progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    holder.progressBar.setVisibility(View.GONE);
                    Log.d("err", "err--fail");
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
        return books.size();
    }


    class HolderSearchBookUser extends RecyclerView.ViewHolder {

        ImageView imageView;
        ProgressBar progressBar;
        TextView bookNameTv, priceTv, starTv, categoryTv, dateTv, authorTv;

        public HolderSearchBookUser(@NonNull View itemView) {
            super(itemView);

            imageView = binding.imageView;
            progressBar = binding.progressBar;
            bookNameTv = binding.bookNameTv;
            priceTv = binding.priceTv;
            starTv = binding.starTv;
            categoryTv = binding.categoryTv;
            dateTv = binding.dateTv;
            authorTv = binding.authorTv;
        }


    }
}
