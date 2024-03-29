package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.PurchasedBookApi.purchasedBookApi;
import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.RowUserBookLibraryBinding;
import com.anhtuan.bookapp.domain.UserBookLibrary;
import com.anhtuan.bookapp.response.ImageResponse;
import com.anhtuan.bookapp.response.NoDataResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterHistoryReadBook extends RecyclerView.Adapter<AdapterHistoryReadBook.HolderHistoryReadBookLibrary>{

    private Context context;
    public List<UserBookLibrary> bookList;
    public RowUserBookLibraryBinding binding;

    public AdapterHistoryReadBook(List<UserBookLibrary> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public HolderHistoryReadBookLibrary onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
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

        if (!bookImage.isBlank()){
            String path = Constant.IP_SERVER_IMAGE + bookImage;
            Glide.with(context)
                    .load(path)
                    .signature(new ObjectKey(path))
                    .into(holder.imageView);
        }

        holder.progressBar.setVisibility(View.GONE);

        holder.unShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchasedBookApi.unShowBook(bookId).enqueue(new RetrofitCallBack<NoDataResponse>() {
                    @Override
                    public void onSuccess(NoDataResponse response) {
                        if (!Objects.isNull(response) &&  response.getCode() == 100){
                            bookList.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {

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
