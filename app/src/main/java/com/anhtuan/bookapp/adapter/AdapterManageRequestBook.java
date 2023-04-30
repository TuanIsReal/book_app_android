package com.anhtuan.bookapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.databinding.RowManageBookRequestBinding;
import com.anhtuan.bookapp.domain.BookRequestUp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterManageRequestBook extends RecyclerView.Adapter<AdapterManageRequestBook.HolderManageRequestBook>{
    public Context context;
    public List<BookRequestUp> bookRequestUpList;
    private RowManageBookRequestBinding binding;
    private ManageRequestBookListener manageRequestBookListener;

    public AdapterManageRequestBook(Context context, List<BookRequestUp> bookRequestUpList) {
        this.context = context;
        this.bookRequestUpList = bookRequestUpList;
    }

    public void setManageRequestBookListener(ManageRequestBookListener manageRequestBookListener) {
        this.manageRequestBookListener = manageRequestBookListener;
    }

    @NonNull
    @Override
    public HolderManageRequestBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowManageBookRequestBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderManageRequestBook(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderManageRequestBook holder, int position) {
        BookRequestUp book = bookRequestUpList.get(position);
        String bookName = book.getBookName();
        String author = book.getAuthor();
        int price = book.getBookPrice();
        long time = book.getRequestTime();
        List<String> category = book.getBookCategory();

        holder.bookNameTv.setText(bookName);
        holder.authorTv.setText(author);
        holder.priceTv.setText(Integer.toString(price));
        holder.dateTv.setText(Utils.covertLongToTimeString(System.currentTimeMillis() - time));
        holder.categoryTv.setText(Utils.toStringCategory(category));
    }

    @Override
    public int getItemCount() {
        return bookRequestUpList.size();
    }

    class HolderManageRequestBook extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView bookNameTv, authorTv, categoryTv, dateTv, priceTv;

        public HolderManageRequestBook(@NonNull View itemView) {
            super(itemView);
            bookNameTv = binding.bookNameTv;
            authorTv = binding.authorTv;
            categoryTv = binding.categoryTv;
            dateTv = binding.dateTv;
            priceTv = binding.priceTv;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            manageRequestBookListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface ManageRequestBookListener{
        void onItemClick(View view,int position);
    }

}
