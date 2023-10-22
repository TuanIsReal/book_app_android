package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.STFApi.stfApi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anhtuan.bookapp.databinding.ColumnNewBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterNewBook extends RecyclerView.Adapter<AdapterNewBook.HolderNewBook> {
    private Context context;
    public List<Book> bookList;
    public ColumnNewBookBinding binding;
    private NewBookListener newBookListener;

    public AdapterNewBook(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    public void setNewBookListener(NewBookListener newBookListener) {
        this.newBookListener = newBookListener;
    }

    @NonNull
    @Override
    public HolderNewBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ColumnNewBookBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderNewBook(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNewBook holder, int position) {
        Book book = bookList.get(position);
        String bookImage = book.getBookImage();
        if (bookImage != null && !bookImage.isBlank()){
            stfApi.getThumbnail(bookImage).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()){
                        Glide.with(context)
                                .load(response.body())
                                .into(holder.bookImage);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }


    class HolderNewBook extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView bookImage;
        public HolderNewBook(@NonNull View itemView) {
            super(itemView);
            bookImage = binding.bookImage;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            newBookListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface NewBookListener{
        void onItemClick(View view, int position);
    }
}
