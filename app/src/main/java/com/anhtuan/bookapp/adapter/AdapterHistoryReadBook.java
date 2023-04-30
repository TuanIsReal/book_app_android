package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.BookApi.bookApi;

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

import com.anhtuan.bookapp.activity.ViewBookActivity;
import com.anhtuan.bookapp.databinding.RowUserBookLibraryBinding;
import com.anhtuan.bookapp.domain.UserBookLibrary;
import com.bumptech.glide.Glide;
import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterHistoryReadBook extends RecyclerView.Adapter<AdapterHistoryReadBook.HolderHistoryReadBookLibrary>{

    private Context context;
    public List<UserBookLibrary> bookList;
    public RowUserBookLibraryBinding binding;

    public AdapterHistoryReadBook(Context context, List<UserBookLibrary> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public HolderHistoryReadBookLibrary onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowUserBookLibraryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderHistoryReadBookLibrary(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderHistoryReadBookLibrary holder, int position) {
        UserBookLibrary book = bookList.get(position);
        String bookId = book.getBookId();
        String bookName = book.getBookName();
        int lastRead = book.getLastRead();
        int totalChapter = book.getTotalChapter();
        String bookImage = book.getBookImage();

        holder.bookNameTv.setText(bookName);
        holder.lastReadTv.setText("Đã đọc " + lastRead + "/" + totalChapter);

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
                intent.putExtra("bookId", bookId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    class HolderHistoryReadBookLibrary extends RecyclerView.ViewHolder {

        ImageView imageView;
        ProgressBar progressBar;
        TextView bookNameTv, lastReadTv;

        ImageButton moreBtn;

        public HolderHistoryReadBookLibrary(@NonNull View itemView) {
            super(itemView);

            imageView = binding.imageView;
            progressBar = binding.progressBar;
            bookNameTv = binding.bookNameTv;
            lastReadTv = binding.lastReadTv;
            moreBtn = binding.moreBtn;
        }


    }
}
