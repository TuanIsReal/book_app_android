package com.anhtuan.bookapp.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.RowBookFilterBinding;
import com.anhtuan.bookapp.domain.Book;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;


public class AdapterBookFilter extends RecyclerView.Adapter<AdapterBookFilter.HolderBookFilter>{

    private Context context;
    private List<Book> books;
    private RowBookFilterBinding binding;

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public AdapterBookFilter(List<Book> books) {
        this.books = books;
    }


    @NonNull
    @Override
    public HolderBookFilter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        binding = RowBookFilterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderBookFilter(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBookFilter holder, int position) {
        Book book = books.get(position);
        String bookName = book.getBookName();
        String author = book.getAuthor();
        int purchased = book.getTotalPurchased();
        double star = book.getStar();
        List<String> category = book.getBookCategory();
        holder.bookNameTv.setText(bookName);
        holder.authorTv.setText(author);
        holder.purchasedTv.setText(Integer.toString(purchased));
        holder.starTv.setText(Double.toString(star));
        holder.categoryTv.setText(Utils.toStringCategory(category));
        String bookImage = book.getBookImage();

        if (!bookImage.isBlank()) {
            String path = Constant.IP_SERVER_IMAGE + bookImage;
            Glide.with(context)
                    .load(path)
                    .signature(new ObjectKey(path))
                    .into(holder.imageView);
        }

        holder.progressBar.setVisibility(View.GONE);

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


    class HolderBookFilter extends RecyclerView.ViewHolder {

        ImageView imageView;
        ProgressBar progressBar;
        TextView bookNameTv, purchasedTv, starTv, categoryTv, authorTv;

        public HolderBookFilter(@NonNull View itemView) {
            super(itemView);

            imageView = binding.imageView;
            progressBar = binding.progressBar;
            bookNameTv = binding.bookNameTv;
            purchasedTv = binding.purchasedTv;
            starTv = binding.starTv;
            categoryTv = binding.categoryTv;
            authorTv = binding.authorTv;
        }


    }
}
