package com.anhtuan.bookapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.databinding.RowBookChapterBinding;
import com.anhtuan.bookapp.domain.BookChapter;

import java.util.List;

public class AdapterBookChapterList extends RecyclerView.Adapter<AdapterBookChapterList.HolderBookChapterList>{

    private Context context;
    public List<BookChapter> bookChapterList;
    public RowBookChapterBinding binding;
    private int lastReadChapter;

    private BookChapterItemListener bookChapterItemListener;

    public AdapterBookChapterList(Context context, List<BookChapter> bookChapterList, int lastReadChapter) {
        this.context = context;
        this.bookChapterList = bookChapterList;
        this.lastReadChapter = lastReadChapter;
    }

    public void setBookChapterItemListener(BookChapterItemListener bookChapterItemListener) {
        this.bookChapterItemListener = bookChapterItemListener;
    }

    @NonNull
    @Override
    public HolderBookChapterList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowBookChapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderBookChapterList(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBookChapterList holder, int position) {
        BookChapter bookChapter = bookChapterList.get(position);
        int chapterNumber = bookChapter.getChapterNumber();
        String chapterName = bookChapter.getChapterName();
        holder.sttTv.setText(String.valueOf(position + 1));
        holder.chapterNameTv.setText("  Chương " + chapterNumber + ": " + chapterName);
        if (lastReadChapter != -1 && chapterNumber == lastReadChapter){
            holder.sttTv.setTextColor(context.getResources().getColor(R.color.black));
            holder.sttTv.setTextSize(17);
            holder.chapterNameTv.setTextColor(context.getResources().getColor(R.color.black));
            holder.chapterNameTv.setTextSize(17);
        }
    }

    @Override
    public int getItemCount() {
        return bookChapterList.size();
    }


    class HolderBookChapterList extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView sttTv, chapterNameTv;

        public HolderBookChapterList(@NonNull View itemView) {
            super(itemView);
            sttTv = binding.sttTv;
            chapterNameTv = binding.chapterNameTv;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            bookChapterItemListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface BookChapterItemListener{
        void onItemClick(View view,int position);
    }
}
