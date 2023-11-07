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

import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.databinding.RowWarningChapterBinding;
import com.anhtuan.bookapp.domain.Book;
import com.anhtuan.bookapp.domain.BookChapter;
import com.anhtuan.bookapp.domain.WarningChapter;
import com.anhtuan.bookapp.response.GetChapterResponse;
import com.anhtuan.bookapp.response.GetUsernameResponse;
import com.anhtuan.bookapp.response.ViewBookResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        bookChapterApi.getChapterInfo(warningChapter.getChapter()).enqueue(new Callback<GetChapterResponse>() {
            @Override
            public void onResponse(Call<GetChapterResponse> call, Response<GetChapterResponse> response) {
                if (response.body() != null && response.body().getCode() == 100){
                    BookChapter bookChapter = response.body().getData();
                    holder.chapterTv.setText("Chương " + String.valueOf(bookChapter.getChapterNumber()) + ": " + bookChapter.getChapterName());

                    bookApi.getBookById(bookChapter.getBookId()).enqueue(new Callback<ViewBookResponse>() {
                        @Override
                        public void onResponse(Call<ViewBookResponse> call, Response<ViewBookResponse> response) {
                            if (response.body() != null && response.body().getCode() == 100){
                                Book book = response.body().getData();
                                holder.bookNameTv.setText(book.getBookName());
                                holder.categoryTv.setText(Utils.toStringCategory(book.getBookCategory()));

                                userApi.getUsername(book.getAuthor()).enqueue(new Callback<GetUsernameResponse>() {
                                    @Override
                                    public void onResponse(Call<GetUsernameResponse> call, Response<GetUsernameResponse> response) {
                                        if (response.body().getCode() == 100){
                                            holder.authorTv.setText(response.body().getData());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<GetUsernameResponse> call, Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<ViewBookResponse> call, Throwable t) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<GetChapterResponse> call, Throwable t) {

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
