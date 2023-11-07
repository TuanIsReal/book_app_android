package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.STFApi.stfApi;

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

import com.anhtuan.bookapp.activity.BookChapterAddActivity;
import com.anhtuan.bookapp.activity.UpdateBookInfoActivity;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.RowSuccessUploadBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.ImageResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

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
        holder.numberPurchasesTv.setText(String.valueOf(book.getTotalPurchased()));
        if (book.getStatus() == 2){
            holder.statusTv.setText("Đã hoàn thành");
        }
        if (bookImage.isBlank()){
            holder.progressBar.setVisibility(View.GONE);
        } else {
            stfApi.getThumbnail(bookImage).enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                    holder.progressBar.setVisibility(View.GONE);
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
                    holder.progressBar.setVisibility(View.GONE);
                    Log.d("err", "err--fail");
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book.getStatus() == 1){
                    Intent intent = new Intent(context, BookChapterAddActivity.class);
                    intent.putExtra("bookName", bookName);
                    context.startActivity(intent);
                }
            }
        });

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book.getStatus() == 1){
                    Intent intent = new Intent(context, UpdateBookInfoActivity.class);
                    intent.putExtra("uploadBookId", book.getId());
                    context.startActivity(intent);
                }
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
        TextView bookNameTv, numberPurchasesTv, categoryTv, dateTv, priceTv, statusTv;
        ImageButton moreBtn;

        public HolderSuccessUploadBook(@NonNull View itemView) {
            super(itemView);
            imageView = binding.imageView;
            progressBar = binding.progressBar;
            bookNameTv = binding.bookNameTv;
            numberPurchasesTv = binding.numberPurchasesTv;
            categoryTv = binding.categoryTv;
            dateTv = binding.dateTv;
            priceTv = binding.priceTv;
            moreBtn = binding.moreBtn;
            statusTv = binding.statusTv;
        }
    }

}
