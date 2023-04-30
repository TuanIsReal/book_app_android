package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.BookRequestUpApi.bookRequestUpApi;

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

import com.anhtuan.bookapp.activity.BookChapterAddActivity;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.databinding.RowSuccessUploadBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.NumberResponse;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSuccessUploadBook extends RecyclerView.Adapter<AdapterSuccessUploadBook.HolderSuccessUploadBook>{
    private Context context;
    public List<Book> bookList;
    private RowSuccessUploadBookBinding binding;
    private String userId;

    public AdapterSuccessUploadBook(Context context, List<Book> bookList, String userId) {
        this.context = context;
        this.bookList = bookList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public HolderSuccessUploadBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowSuccessUploadBookBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderSuccessUploadBook(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSuccessUploadBook holder, int position) {
        Book book = bookList.get(position);
        String bookName = book.getBookName();
        int price = book.getBookPrice();
        long time = book.getUploadTime();
        List<String> category = book.getBookCategory();
        String bookImage = book.getBookImage();

        holder.bookNameTv.setText(bookName);
        holder.priceTv.setText(Integer.toString(price));
        holder.dateTv.setText(Utils.covertLongToTimeString(System.currentTimeMillis() - time));
        holder.categoryTv.setText(Utils.toStringCategory(category));

        bookRequestUpApi.getQuantityPurchased(book.getId(), userId).enqueue(new Callback<NumberResponse>() {
            @Override
            public void onResponse(Call<NumberResponse> call, Response<NumberResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    holder.numberPurchasesTv.setText(String.valueOf(response.body().getData()));

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

                }
            }

            @Override
            public void onFailure(Call<NumberResponse> call, Throwable t) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookChapterAddActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    class HolderSuccessUploadBook extends RecyclerView.ViewHolder{

        ImageView imageView;
        ProgressBar progressBar;
        TextView bookNameTv, numberPurchasesTv, categoryTv, dateTv, priceTv;

        public HolderSuccessUploadBook(@NonNull View itemView) {
            super(itemView);
            imageView = binding.imageView;
            progressBar = binding.progressBar;
            bookNameTv = binding.bookNameTv;
            numberPurchasesTv = binding.numberPurchasesTv;
            categoryTv = binding.categoryTv;
            dateTv = binding.dateTv;
            priceTv = binding.priceTv;
        }
    }

}
