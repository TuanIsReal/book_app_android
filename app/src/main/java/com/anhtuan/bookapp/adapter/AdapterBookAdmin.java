package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.STFApi.stfApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.activity.ChapterAddAdminActivity;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.databinding.RowBookAdminBinding;
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

public class AdapterBookAdmin extends RecyclerView.Adapter<AdapterBookAdmin.HolderBookAdmin>{

    private Context context;
    private ArrayList<Book> books;

    private RowBookAdminBinding binding;


    public AdapterBookAdmin(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public HolderBookAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowBookAdminBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderBookAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBookAdmin holder, int position) {
        Book book = books.get(position);
        String bookName = book.getBookName();
        String introduction = book.getIntroduction();
        String author = book.getAuthor();
        int price = book.getBookPrice();
        double star = book.getStar();
        long time = book.getUploadTime();
        List<String> category = book.getBookCategory();

        //
        holder.bookNameTv.setText(bookName);
        holder.introductionTv.setText(introduction);
        userApi.getUsername(author).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    holder.authorTv.setText(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        holder.priceTv.setText(Integer.toString(price));
        holder.starTv.setText(Double.toString(star));
        holder.dateTv.setText(Utils.covertLongToTimeString(System.currentTimeMillis() - time));
        holder.categoryTv.setText(Utils.toStringCategory(category));
        String bookImage = book.getBookImage();

        if (bookImage.isBlank()){
            holder.progressBar.setVisibility(View.GONE);
        } else {
            stfApi.getThumbnail(bookImage).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    holder.progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        Glide.with(context)
                                .load(response.body())
                                .into(holder.imageView);
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    holder.progressBar.setVisibility(View.GONE);
                    Log.d("err", "err--fail");
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChapterAddAdminActivity.class);
                intent.putExtra("bookName", bookName);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    class HolderBookAdmin extends RecyclerView.ViewHolder {

        ImageView imageView;
        ProgressBar progressBar;
        TextView bookNameTv, priceTv, starTv, introductionTv, categoryTv, dateTv, authorTv;
        ImageButton moreBtn;


        public HolderBookAdmin(@NonNull View itemView) {
            super(itemView);

            imageView = binding.imageView;
            progressBar = binding.progressBar;
            bookNameTv = binding.bookNameTv;
            priceTv = binding.priceTv;
            starTv = binding.starTv;
            introductionTv = binding.introductionTv;
            categoryTv = binding.categoryTv;
            dateTv = binding.dateTv;
            authorTv = binding.authorTv;
            moreBtn = binding.moreBtn;

        }


    }

}
