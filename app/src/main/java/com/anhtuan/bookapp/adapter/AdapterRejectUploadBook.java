package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.BookRequestUpApi.bookRequestUpApi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.databinding.RowRejectUploadBookBinding;
import com.anhtuan.bookapp.domain.BookRequestUp;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterRejectUploadBook extends RecyclerView.Adapter<AdapterRejectUploadBook.HolderRejectUploadBook> {

    private Context context;
    public List<BookRequestUp> bookRejectList;
    private RowRejectUploadBookBinding binding;


    public AdapterRejectUploadBook(Context context, List<BookRequestUp> bookRejectList) {
        this.context = context;
        this.bookRejectList = bookRejectList;
    }

    @NonNull
    @Override
    public HolderRejectUploadBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowRejectUploadBookBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderRejectUploadBook(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRejectUploadBook holder, int position) {
        BookRequestUp book = bookRejectList.get(position);
        String bookName = book.getBookName();
        int price = book.getBookPrice();
        long time = book.getRequestTime();
        List<String> category = book.getBookCategory();
        String bookImage = book.getBookImage();

        holder.bookNameTv.setText(bookName);
        holder.priceTv.setText(Integer.toString(price));
        holder.dateTv.setText(Utils.covertLongToTimeString(System.currentTimeMillis() - time));

        holder.categoryTv.setText(Utils.toStringCategory(category));

        if (bookImage != null){
            bookRequestUpApi.getBookRequestUpImage(bookImage).enqueue(new Callback<ResponseBody>() {
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
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    holder.progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            holder.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return bookRejectList.size();
    }

    class HolderRejectUploadBook extends RecyclerView.ViewHolder{

        ImageView imageView;
        ProgressBar progressBar;
        TextView bookNameTv, categoryTv, dateTv, priceTv;

        public HolderRejectUploadBook(@NonNull View itemView) {
            super(itemView);
            imageView = binding.imageView;
            progressBar = binding.progressBar;
            bookNameTv = binding.bookNameTv;
            categoryTv = binding.categoryTv;
            dateTv = binding.dateTv;
            priceTv = binding.priceTv;
        }
    }

}
