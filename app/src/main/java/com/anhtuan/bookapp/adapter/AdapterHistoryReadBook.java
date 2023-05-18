package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.activity.ViewBookActivity;
import com.anhtuan.bookapp.databinding.RowUserBookLibraryBinding;
import com.anhtuan.bookapp.domain.UserBookLibrary;
import com.anhtuan.bookapp.response.BaseResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.bumptech.glide.Glide;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterHistoryReadBook extends RecyclerView.Adapter<AdapterHistoryReadBook.HolderHistoryReadBookLibrary>{

    private Context context;
    public List<UserBookLibrary> bookList;
    public RowUserBookLibraryBinding binding;
    private String userId;

    public AdapterHistoryReadBook(Context context, List<UserBookLibrary> bookList, String userId) {
        this.context = context;
        this.bookList = bookList;
        this.userId= userId;
    }

    @NonNull
    @Override
    public HolderHistoryReadBookLibrary onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowUserBookLibraryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderHistoryReadBookLibrary(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderHistoryReadBookLibrary holder, @SuppressLint("RecyclerView") int position) {
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

        holder.unShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchasedBookApi.unShowBook(bookId, userId).enqueue(new Callback<NoDataResponse>() {
                    @Override
                    public void onResponse(Call<NoDataResponse> call, Response<NoDataResponse> response) {
                        if (!Objects.isNull(response.body()) &&  response.body().getCode() == 100){
                            bookList.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<NoDataResponse> call, Throwable t) {
                        Toast.makeText(context, ""+t, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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

        ImageButton unShowBtn;

        public HolderHistoryReadBookLibrary(@NonNull View itemView) {
            super(itemView);

            imageView = binding.imageView;
            progressBar = binding.progressBar;
            bookNameTv = binding.bookNameTv;
            lastReadTv = binding.lastReadTv;
            unShowBtn = binding.unShowBtn;
        }


    }
}
