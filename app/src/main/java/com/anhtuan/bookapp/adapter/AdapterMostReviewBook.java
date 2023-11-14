package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.UnAuthApi.unAuthApi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anhtuan.bookapp.activity.ViewBookActivity;
import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.ColumnMostReviewBookBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.response.ImageResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMostReviewBook extends RecyclerView.Adapter<AdapterMostReviewBook.HolderMostReviewBook> {
    private Context context;
    public List<Book> bookList;
    public ColumnMostReviewBookBinding binding;


    public AdapterMostReviewBook(List<Book> bookList) {
        this.bookList = bookList;
    }


    @NonNull
    @Override
    public HolderMostReviewBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        binding = ColumnMostReviewBookBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderMostReviewBook(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMostReviewBook holder, int position) {
        Book book = bookList.get(position);
        String bookImage = book.getBookImage();
        holder.bookName.setText(book.getBookName());
        holder.ratingPoint.setText(String.valueOf(book.getStar()));
        holder.ratingBar.setRating((float) book.getStar());

        if (bookImage != null && !bookImage.isBlank()){
            String path = Constant.IP_SERVER_IMAGE + bookImage;
            Glide.with(context)
                    .load(path)
                    .signature(new ObjectKey(path))
                    .into(holder.bookImage);
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


    class HolderMostReviewBook extends RecyclerView.ViewHolder{
        ImageView bookImage;
        TextView bookName, ratingPoint;
        RatingBar ratingBar;
        public HolderMostReviewBook(@NonNull View itemView) {
            super(itemView);
            bookImage = binding.bookImage;
            bookName = binding.bookName;
            ratingPoint = binding.ratingPoint;
            ratingBar = binding.ratingBar;
        }

    }

}
