package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.STFApi.stfApi;

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
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.RowRequestUploadBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.ImageResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterRequestUploadBook extends RecyclerView.Adapter<AdapterRequestUploadBook.HolderRequestUploadBook>{

    private Context context;
    public List<Book> bookRequestUpList;
    private RowRequestUploadBookBinding binding;


    public AdapterRequestUploadBook(Context context, List<Book> bookRequestUpList) {
        this.context = context;
        this.bookRequestUpList = bookRequestUpList;
    }

    @NonNull
    @Override
    public HolderRequestUploadBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowRequestUploadBookBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderRequestUploadBook(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRequestUploadBook holder, int position) {
        Book book = bookRequestUpList.get(position);
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
                }
            });
        } else {
            holder.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return bookRequestUpList.size();
    }

    class HolderRequestUploadBook extends RecyclerView.ViewHolder{

        ImageView imageView;
        ProgressBar progressBar;
        TextView bookNameTv, categoryTv, dateTv, priceTv;

        public HolderRequestUploadBook(@NonNull View itemView) {
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
