package com.anhtuan.bookapp.adapter;

import static com.anhtuan.bookapp.api.BookApi.bookApi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.common.Utils;
import com.anhtuan.bookapp.databinding.RowReportBinding;
import com.anhtuan.bookapp.domain.Report;
import com.anhtuan.bookapp.response.ViewBookResponse;

import java.util.List;

public class AdapterManageReport extends RecyclerView.Adapter<AdapterManageReport.HolderManageReport> {

    Context context;
    List<Report> reports;
    RowReportBinding binding;
    ManageReportListener manageReportListener;

    public AdapterManageReport(List<Report> reports) {
        this.reports = reports;
    }

    public void setManageReportListener(ManageReportListener manageReportListener) {
        this.manageReportListener = manageReportListener;
    }

    @NonNull
    @Override
    public HolderManageReport onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        binding = RowReportBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderManageReport(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderManageReport holder, int position) {
        Report report = reports.get(position);
        if (report.getBookId() != null){
            bookApi.getBookById(report.getBookId()).enqueue(new RetrofitCallBack<ViewBookResponse>() {
                @Override
                public void onSuccess(ViewBookResponse response) {
                    if (response.getCode() == 100){
                        holder.bookNameTv.setText(response.getData().getBookName());
                    }
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        }
        int type = report.getType();
        switch (type){
            case 1:
                holder.reportTypeTv.setText("Nội dung người lớn");
                break;
            case 2:
                holder.reportTypeTv.setText("Gây thù hằn");
                break;
            case 3:
                holder.reportTypeTv.setText("Công kích cá nhân");
                break;
            case 4:
                holder.reportTypeTv.setText("Spam");
                break;
            case 5:
                holder.reportTypeTv.setText("Các vấn đề khác");
                break;
        }

        holder.timeTv.setText(Utils.covertLongToTimeString(System.currentTimeMillis() - report.getReportTime()));

    }

    @Override
    public int getItemCount() {
        return reports.size();
    }


    class HolderManageReport extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView bookNameTv, reportTypeTv, timeTv;

        public HolderManageReport(@NonNull View itemView) {
            super(itemView);
            bookNameTv = binding.bookNameTv;
            reportTypeTv = binding.reportTypeTv;
            timeTv = binding.timeTv;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            manageReportListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface ManageReportListener{
        void onItemClick(View view,int position);
    }
}
