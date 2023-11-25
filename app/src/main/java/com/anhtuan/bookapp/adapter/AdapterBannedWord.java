package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.BookChapterApi.bookChapterApi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.RowBannedWordBinding;
import com.anhtuan.bookapp.response.NoDataResponse;

import java.util.List;

public class AdapterBannedWord extends RecyclerView.Adapter<AdapterBannedWord.HolderBannedWord> {

    private Context context;
    public List<String> bannedWords;
    private RowBannedWordBinding binding;

    public AdapterBannedWord(List<String> bannedWords) {
        this.bannedWords = bannedWords;
    }

    @NonNull
    @Override
    public HolderBannedWord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        binding = RowBannedWordBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderBannedWord(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBannedWord holder, int position) {
        String bannedWord = bannedWords.get(position);
        holder.bannedWordTv.setText(bannedWord);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookChapterApi.removeBannedWord(bannedWord).enqueue(new RetrofitCallBack<NoDataResponse>() {
                    @Override
                    public void onSuccess(NoDataResponse response) {
                        if (response.getCode() == 100){
                            bannedWords.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return bannedWords.size();
    }


    class HolderBannedWord extends RecyclerView.ViewHolder{

        TextView bannedWordTv;
        ImageButton deleteBtn;

        public HolderBannedWord(@NonNull View itemView) {
            super(itemView);
            bannedWordTv = binding.bannedWordTv;
            deleteBtn = binding.deleteBtn;
        }
    }
}
