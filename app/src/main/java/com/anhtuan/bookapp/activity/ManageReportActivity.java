package com.anhtuan.bookapp.activity;

import static com.anhtuan.bookapp.api.ReportApi.reportApi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.adapter.AdapterManageReport;
import com.anhtuan.bookapp.api.RetrofitCallBack;
import com.anhtuan.bookapp.databinding.ActivityManageReportBinding;
import com.anhtuan.bookapp.domain.Report;
import com.anhtuan.bookapp.response.GetReportsResponse;

import java.util.List;

public class ManageReportActivity extends AppCompatActivity implements AdapterManageReport.ManageReportListener{

    ActivityManageReportBinding binding;
    List<Report> reports;
    AdapterManageReport adapterManageReport;
    private final static int REQUEST_CODE = 100018;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityManageReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setReportList();

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setReportList();
            }
        });

        binding.returnBtn.setOnClickListener(v -> onBackPressed());
    }

    private void setReportList() {
        reportApi.getReports(0).enqueue(new RetrofitCallBack<GetReportsResponse>() {
            @Override
            public void onSuccess(GetReportsResponse response) {
                binding.swipeRefresh.setRefreshing(false);
                if (response.getCode() == 100){
                    reports = response.getData();
                    adapterManageReport = new AdapterManageReport(reports);
                    adapterManageReport.setManageReportListener(ManageReportActivity.this);
                    binding.reportsRv.setAdapter(adapterManageReport);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            setReportList();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Report report = reports.get(position);
        Intent intent = new Intent(view.getContext(), ReportDetailActivity.class);
        intent.putExtra("report", report);
        startActivityForResult(intent, REQUEST_CODE);
    }
}