package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.BookApi.bookApi;
import static com.anhtuan.bookapp.api.BookChapterApi.bookChapterApi;
import static com.anhtuan.bookapp.api.UserApi.userApi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.databinding.RowWarningChapterBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.domain.BookChapter;
import com.anhtuan.bookapp.domain.WarningChapter;
import com.anhtuan.bookapp.response.GetChapterResponse;
import com.anhtuan.bookapp.response.GetUsernameResponse;
import com.anhtuan.bookapp.response.GetWarningListData;
import com.anhtuan.bookapp.response.ViewBookResponse;
import java.util.List;

public class AdapterManageWarning extends RecyclerView.Adapter<AdapterManageWarning.HolderManageWarning>{
    public Context context;
    public List<GetWarningListData> warningChapters;
    public RowWarningChapterBinding binding;
    private ManageWarningListener manageWarningListener;

    public AdapterManageWarning(Context context, List<GetWarningListData> warningChapters) {
        this.context = context;
        this.warningChapters = warningChapters;
    }

    public void setManageWarningListener(ManageWarningListener manageWarningListener) {
        this.manageWarningListener = manageWarningListener;
    }

    @NonNull
    @Override
    public HolderManageWarning onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowWarningChapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderManageWarning(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderManageWarning holder, int position) {
        GetWarningListData warningData = warningChapters.get(position);
        holder.bookNameTv.setText(warningData.getBook().getBookName());
        holder.authorTv.setText(warningData.getBook().getAuthor());
        holder.chapterTv.setText(warningData.getChapter().getChapterName());
    }

    @Override
    public int getItemCount() {
        return warningChapters.size();
    }


    class HolderManageWarning extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView bookNameTv, authorTv, chapterTv;

        public HolderManageWarning(@NonNull View itemView) {
            super(itemView);
            bookNameTv = binding.bookNameTv;
            authorTv = binding.authorTv;
            chapterTv = binding.chapterTv;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            manageWarningListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface ManageWarningListener{
        void onItemClick(View view,int position);
    }
}
