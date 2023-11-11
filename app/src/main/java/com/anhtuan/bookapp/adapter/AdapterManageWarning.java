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
import com.anhtuan.bookapp.response.ViewBookResponse;
import java.util.List;

public class AdapterManageWarning extends RecyclerView.Adapter<AdapterManageWarning.HolderManageWarning>{
    public Context context;
    public List<WarningChapter> warningChapters;
    public RowWarningChapterBinding binding;

    public AdapterManageWarning(Context context, List<WarningChapter> warningChapters) {
        this.context = context;
        this.warningChapters = warningChapters;
    }

    @NonNull
    @Override
    public HolderManageWarning onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowWarningChapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderManageWarning(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderManageWarning holder, int position) {
        WarningChapter warningChapter = warningChapters.get(position);

        bookChapterApi.getChapterInfo(warningChapter.getChapter()).enqueue(new RetrofitCallBack<GetChapterResponse>() {
            @Override
            public void onSuccess(GetChapterResponse response) {
                if (response != null && response.getCode() == 100){
                    BookChapter bookChapter = response.getData();
                    holder.chapterTv.setText("Chương " + String.valueOf(bookChapter.getChapterNumber()) + ": " + bookChapter.getChapterName());

                    bookApi.getBookById(bookChapter.getBookId()).enqueue(new RetrofitCallBack<ViewBookResponse>() {
                        @Override
                        public void onSuccess(ViewBookResponse response) {
                            if (response != null && response.getCode() == 100){
                                Book book = response.getData();
                                holder.bookNameTv.setText(book.getBookName());
                                holder.categoryTv.setText(Utils.toStringCategory(book.getBookCategory()));

                                userApi.getUsername(book.getAuthor()).enqueue(new RetrofitCallBack<GetUsernameResponse>() {
                                    @Override
                                    public void onSuccess(GetUsernameResponse response) {
                                        if (response.getCode() == 100){
                                            holder.authorTv.setText(response.getData());
                                        }
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                    }
                                });
                            }
                        }
                        @Override
                        public void onFailure(String errorMessage) {
                        }
                    });
                }
            }
            @Override
            public void onFailure(String errorMessage) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return warningChapters.size();
    }


    class HolderManageWarning extends RecyclerView.ViewHolder {
        TextView bookNameTv, authorTv, chapterTv, categoryTv;

        public HolderManageWarning(@NonNull View itemView) {
            super(itemView);
            bookNameTv = binding.bookNameTv;
            authorTv = binding.authorTv;
            categoryTv = binding.categoryTv;
            chapterTv = binding.chapterTv;
        }
    }
}
