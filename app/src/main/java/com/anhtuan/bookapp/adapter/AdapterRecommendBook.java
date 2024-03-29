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
import com.anhtuan.bookapp.databinding.ColumnRecommendBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.ImageResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterRecommendBook extends RecyclerView.Adapter<AdapterRecommendBook.HolderRecommendBook> {
    private Context context;
    public List<Book> bookList;
    public ColumnRecommendBookBinding binding;


    public AdapterRecommendBook(List<Book> bookList) {
        this.bookList = bookList;
    }


    @NonNull
    @Override
    public HolderRecommendBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
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
            String path = Constant.IP_SERVER_IMAGE + bookImage;
            Glide.with(context)
                    .load(path)
                    .signature(new ObjectKey(path))
                    .into(holder.recommendImageIv);
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
