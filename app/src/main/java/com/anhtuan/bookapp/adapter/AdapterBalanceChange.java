package com.anhtuan.bookapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.config.Constant;
import com.anhtuan.bookapp.databinding.RowBalanceChangeBinding;
import com.anhtuan.bookapp.domain.TransactionHistory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterBalanceChange extends RecyclerView.Adapter<AdapterBalanceChange.HolderBalanceChange>{

    private Context context;
    private List<TransactionHistory> transactionHistoryList;
    private RowBalanceChangeBinding binding;

    public AdapterBalanceChange(Context context, List<TransactionHistory> transactionHistoryList) {
        this.context = context;
        this.transactionHistoryList = transactionHistoryList;
    }

    @NonNull
    @Override
    public HolderBalanceChange onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowBalanceChangeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderBalanceChange(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBalanceChange holder, int position) {
        TransactionHistory transactionHistory = transactionHistoryList.get(position);
        String text = "";
        if (transactionHistory.getPoint() > 0){
            text = "+";
        }

        holder.point.setText(text+ String.valueOf(transactionHistory.getPoint() + " point"));
        holder.balance.setText(String.valueOf(transactionHistory.getTotalPoint()) + " point");
        holder.content.setText(Constant.createContentTransactionHistory(transactionHistory.getTransactionType()));

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(transactionHistory.getTransactionTime());
        holder.time.setText(format.format(date));
    }

    @Override
    public int getItemCount() {
        return transactionHistoryList.size();
    }

    class HolderBalanceChange extends RecyclerView.ViewHolder{
        TextView point, balance, content, time;
        public HolderBalanceChange(@NonNull View itemView) {
            super(itemView);
            point = binding.point;
            balance = binding.balance;
            content = binding.content;
            time = binding.time;
        }
    }
}
