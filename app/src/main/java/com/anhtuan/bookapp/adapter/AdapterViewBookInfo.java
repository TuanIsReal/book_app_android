package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;

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
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.ColumnSameAuthorBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.ImageResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterViewBookInfo extends RecyclerView.Adapter<AdapterViewBookInfo.HolderViewBook>{

    private Context context;
    private ArrayList<Book> books;
    private ColumnSameAuthorBookBinding binding;

    public AdapterViewBookInfo(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public HolderViewBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ColumnSameAuthorBookBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderViewBook(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderViewBook holder, int position) {
        Book book = books.get(position);
        String bookName = book.getBookName();
        List<String> category = book.getBookCategory();
        String bookImage = book.getBookImage();

        holder.bookNameTv.setText(bookName);
        holder.categoriesTv.setText(Utils.toStringCategory(category));
        if (bookImage != null){
            unAuthApi.getThumbnail(bookImage).enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                    if (response.body().getCode() == 100){
                        String path = Constant.IP_SERVER_IMAGE + response.body().getData();
                        Glide.with(context)
                                .load(path)
                                .signature(new ObjectKey(path))
                                .into(holder.imageView);
                    }
                }

                @Override
                public void onFailure(Call<ImageResponse> call, Throwable t) {
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewBookActivity.class);
                    intent.putExtra("bookId", book.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class HolderViewBook extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView bookNameTv;
        TextView categoriesTv;
        public HolderViewBook(@NonNull View itemView) {
            super(itemView);

            imageView = binding.imageView;
            bookNameTv = binding.bookNameTv;
            categoriesTv = binding.categoriesTv;
        }
    }
}
